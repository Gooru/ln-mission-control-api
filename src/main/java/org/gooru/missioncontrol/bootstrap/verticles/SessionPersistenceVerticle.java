package org.gooru.missioncontrol.bootstrap.verticles;

import org.gooru.missioncontrol.constants.MessagebusEndpoints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import io.vertx.redis.op.SetOptions;


public class SessionPersistenceVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(SessionPersistenceVerticle.class);
  private RedisClient redisClient;
  public static final String MBEP_FAIL_MSG_SESSION_PERSISTENCE =
      "mbus.fail.message.sessionpersistence";
  public static final int MBEP_FAIL_CODE_SESSION_PERSISTENCE = 1;


  @Override
  public void start(Future<Void> startFuture) {
    EventBus eb = this.vertx.eventBus();

    this.initializeVerticle(startFuture);

    eb.<JsonObject>localConsumer(MessagebusEndpoints.MBEP_SESSION_PERSISTENCE, message -> {
      Future<Void> persistSessionFuture = persistSession(message.body());

      persistSessionFuture.setHandler(persistSessionAsyncResult -> {
        if (persistSessionAsyncResult.succeeded()) {
          message.reply(new JsonObject());
        } else {
          message.fail(MBEP_FAIL_CODE_SESSION_PERSISTENCE, MBEP_FAIL_MSG_SESSION_PERSISTENCE);
        }
      });

    }).completionHandler(result -> {
      if (result.succeeded()) {
        LOGGER.info("Session persistence end point ready to listen");
      } else {
        LOGGER.error(
            "Error registering the Session persistence handler. Halting the persistence machinery");
        Runtime.getRuntime().halt(2);
      }
    });
  }

  private Future<Void> persistSession(JsonObject session) {
    Future<Void> future = Future.future();
    String key = session.getString("access_token");
    Long validity = session.getLong("access_token_validity");

    SetOptions options = new SetOptions().setEX(validity);

    this.redisClient.setWithOptions(key, session.toString(), options, asyncResult -> {
      if (asyncResult.succeeded()) {
        LOGGER.debug("Successfully persisted session: '{}'", key);
        future.complete();
      } else {
        LOGGER.error("Session persistence failed: '{}'", key, asyncResult.cause());
        future.fail(asyncResult.cause());
      }
    });
    return future;
  }

  @Override
  public void stop(Future<Void> stopFuture) {
    this.finalizeVerticle(stopFuture);
  }

  private void initializeVerticle(Future<Void> startFuture) {
    try {
      JsonObject configuration = this.config().getJsonObject("redisConfig");
      RedisOptions options = new RedisOptions(configuration);
      this.redisClient = RedisClient.create(this.vertx, options);
      this.redisClient.get("NonExistingKey", initHandler -> {
        if (initHandler.succeeded()) {
          LOGGER.info("Initial connection check with Redis done");
          startFuture.complete();
        } else {
          startFuture.fail(initHandler.cause());
        }
      });
    } catch (Throwable throwable) {
      LOGGER.error("Not able to continue initialization.", throwable);
      startFuture.fail(throwable);
    }
  }

  private void finalizeVerticle(Future<Void> stopFuture) {
    if (this.redisClient != null) {
      this.redisClient.close(redisCloseAsyncHandler -> {
        if (redisCloseAsyncHandler.succeeded()) {
          LOGGER.info("Redis client has been closed successfully");
        } else {
          LOGGER.error("Error in closing redis client", redisCloseAsyncHandler.cause());
        }
        stopFuture.complete();
      });
    }
  }

}
