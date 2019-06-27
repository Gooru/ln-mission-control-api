
package org.gooru.missioncontrol.bootstrap.verticles;

import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.constants.MessagebusEndpoints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/**
 * @author szgooru Created On 19-Jun-2019
 */
public class AuthVerticle extends AbstractVerticle {

  private final static Logger LOGGER = LoggerFactory.getLogger(AuthVerticle.class);
  private RedisClient redisClient;

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    EventBus eb = vertx.eventBus();
    initializeVerticle(startFuture);

    eb.localConsumer(MessagebusEndpoints.MBEP_AUTH, message -> {

      String sessionToken = message.headers().get(MessageConstants.MSG_HEADER_TOKEN);
      Future<JsonObject> fetchSessionFuture = fetchSessionFromRedis(sessionToken);

      fetchSessionFuture.setHandler(sessionAsyncResult -> {
        if (sessionAsyncResult.succeeded()) {
          message.reply(sessionAsyncResult.result(), new DeliveryOptions()
              .addHeader(MessageConstants.MSG_OP_STATUS, MessageConstants.MSG_OP_STATUS_SUCCESS));
          updateSessionExpiryInRedis(sessionToken);
        } else {
          message.reply(new JsonObject(), new DeliveryOptions()
              .addHeader(MessageConstants.MSG_OP_STATUS, MessageConstants.MSG_OP_STATUS_FAIL));
        }
      });

    }).completionHandler(result -> {
      if (result.succeeded()) {
        LOGGER.info("Auth end point ready to listen");
      } else {
        LOGGER.error("Error registering the auth handler. Halting the auth machinery");
        Runtime.getRuntime().halt(2);
      }
    });
  }

  @Override
  public void stop(Future<Void> stopFuture) throws Exception {
    finalizeVerticle(stopFuture);
  }

  private Future<JsonObject> fetchSessionFromRedis(String sessionToken) {
    Future<JsonObject> future = Future.future();

    redisClient.get(sessionToken, redisAsyncResult -> {
      if (redisAsyncResult.succeeded()) {
        final String redisResult = redisAsyncResult.result();
        LOGGER.debug("Redis responded with '{}'", redisResult);
        if (redisResult != null) {
          try {
            JsonObject jsonResult = new JsonObject(redisResult);
            future.complete(jsonResult);
          } catch (DecodeException de) {
            LOGGER.error("exception while decoding json for token '{}'", sessionToken, de);
            future.fail(de);
          }
        } else {
          LOGGER.info("Session not found. Invalid session");
          future.fail("Session not found. Invalid session");
        }
      } else {
        LOGGER.error("Redis operation failed", redisAsyncResult.cause());
        future.fail(redisAsyncResult.cause());
      }
    });
    return future;
  }

  private Future<Void> updateSessionExpiryInRedis(String sessionToken) {
    Future<Void> future = Future.future();
    int sessionTimeout = config().getInteger("sessionTimeoutInSeconds");
    redisClient.expire(sessionToken, sessionTimeout, updateHandler -> {
      if (updateHandler.succeeded()) {
        LOGGER.debug("expiry time of session {} is updated", sessionToken);
      } else {
        LOGGER.warn("Not able to update expiry for key {}", sessionToken, updateHandler.cause());
      }
      future.complete();
    });
    return future;
  }

  private void initializeVerticle(Future<Void> startFuture) {
    try {
      JsonObject configuration = config().getJsonObject("redisConfig");
      RedisOptions options = new RedisOptions(configuration);
      redisClient = RedisClient.create(vertx, options);
      redisClient.get("NonExistingKey", initHandler -> {
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
    if (redisClient != null) {
      redisClient.close(redisCloseAsyncHandler -> {
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
