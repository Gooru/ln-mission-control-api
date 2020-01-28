package org.gooru.missioncontrol.processors.auth.signinuser;

import java.util.ResourceBundle;
import org.gooru.missioncontrol.app.data.EventBusMessage;
import org.gooru.missioncontrol.bootstrap.component.jdbi.DBICreator;
import org.gooru.missioncontrol.constants.HttpConstants;
import org.gooru.missioncontrol.constants.MessagebusEndpoints;
import org.gooru.missioncontrol.processors.MessageProcessor;
import org.gooru.missioncontrol.processors.exceptions.HttpResponseWrapperException;
import org.gooru.missioncontrol.processors.responses.MessageResponse;
import org.gooru.missioncontrol.processors.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;


public class SignInUserProcessor implements MessageProcessor {

  private final Vertx vertx;
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;
  private static final Logger LOGGER = LoggerFactory.getLogger(SignInUserProcessor.class);
  private EventBusMessage eventBusMessage;
  private final SignInUserService signInUserService =
      new SignInUserService(DBICreator.getDbiForAuthDB(), DBICreator.getDbiForDefaultDS());
  private SignInUserCommand command;
  private static ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

  public SignInUserProcessor(Vertx vertx, Message<JsonObject> message) {
    this.vertx = vertx;
    this.message = message;
    this.result = Future.future();
  }


  @Override
  public Future<MessageResponse> process() {
    try {
      this.eventBusMessage = EventBusMessage.eventBusMessageBuilder(message);
      command = SignInUserCommand.builder(eventBusMessage.getRequestHeaders());
      LOGGER.debug("Will process SignIn User command: '{}'", command);
      Future<Session> sessionFuture = signInUser();
      sessionFuture.setHandler(asyncResult -> {
        if (asyncResult.succeeded()) {
          try {
            final Session Session = asyncResult.result();
            Future<Void> sessionPersistenceFuture = persistSession(Session);
            sessionPersistenceFuture.setHandler(asyncPersistenceResult -> {
              if (asyncPersistenceResult.succeeded()) {
                try {
                  this.complete(Session);
                } catch (Exception e) {
                  LOGGER.warn("Error while sending events", e);
                  this.result.fail(e);
                }
              } else {
                LOGGER.warn("Error while persisting session", asyncPersistenceResult.cause());
                this.result.fail(asyncPersistenceResult.cause());
              }
            });
          } catch (Exception e) {
            LOGGER.warn("Error while generating session token", e);
            this.result.fail(e);
          }
        } else {
          LOGGER.warn("Error while triggering session token generation flow", asyncResult.cause());
          this.result.fail(asyncResult.cause());
        }
      });
    } catch (Throwable e) {
      LOGGER.warn("Encountered error while processing", e);
      this.result.fail(e);
    }
    return this.result;
  }



  private void complete(Session session) throws JsonProcessingException {
    String sessionAsJsonString = new ObjectMapper().writeValueAsString(session);
    this.result
        .complete(MessageResponseFactory.createOkayResponse(new JsonObject(sessionAsJsonString)));
  }

  private Future<Void> persistSession(Session token) throws JsonProcessingException {
    Future<Void> future = Future.future();
    String sessionToPersist = new ObjectMapper().writeValueAsString(token);
    LOGGER.debug("session to persist: {}", sessionToPersist);
    vertx.eventBus().send(MessagebusEndpoints.MBEP_SESSION_PERSISTENCE,
        new JsonObject(sessionToPersist), asyncResult -> {
          if (asyncResult.succeeded()) {
            LOGGER.debug("Successfully persisted session");
            future.complete();
          } else {
            LOGGER.warn("session persistence failed: '{}'", token.getAccessToken());
            future.fail(new HttpResponseWrapperException(HttpConstants.HttpStatus.ERROR,
                resourceBundle.getString("internal.error")));
          }
        });
    return future;
  }

  Future<Session> signInUser() {
    Future<Session> result = Future.future();
    vertx.<Session>executeBlocking(blockingFuture -> {
      try {
        Session session = signInUserService.signInUser(this.command);
        blockingFuture.complete(session);
      } catch (Exception e) {
        LOGGER.warn("Not able to create session token", e);
        blockingFuture.fail(e);
      }
    }, asyncResult -> {
      if (asyncResult.succeeded()) {
        result.complete(asyncResult.result());
      } else {
        LOGGER.warn("Failed to create session token", asyncResult.cause());
        result.fail(asyncResult.cause());
      }
    });
    return result;
  }
}
