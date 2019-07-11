
package org.gooru.missioncontrol.bootstrap.verticles;

import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.constants.MessagebusEndpoints;
import org.gooru.missioncontrol.processors.MessageProcessor;
import org.gooru.missioncontrol.processors.MessageProcessorBuilder;
import org.gooru.missioncontrol.processors.exceptions.HttpResponseWrapperException;
import org.gooru.missioncontrol.processors.exceptions.MessageResponseWrapperException;
import org.gooru.missioncontrol.processors.responses.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 27-Jun-2019
 */
public class DispatcherVerticle extends AbstractVerticle {

  private final static Logger LOGGER = LoggerFactory.getLogger(DispatcherVerticle.class);

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    EventBus eb = vertx.eventBus();
    eb.localConsumer(MessagebusEndpoints.MBEP_DISPATCHER, this::processMessage)
        .completionHandler(result -> {
          if (result.succeeded()) {
            LOGGER.info("dispatcher end point ready to listen");
            startFuture.complete();
          } else {
            LOGGER.error("Error registering the Dispatcher handler. Halting the machinery");
            startFuture.fail(result.cause());
            Runtime.getRuntime().halt(1);
          }
        });
  }

  private void processMessage(Message<JsonObject> message) {
    LOGGER.info("received message with body: {}", message.body());
    String op = message.headers().get(MessageConstants.MSG_HEADER_OP);
    MessageProcessor processor = MessageProcessorBuilder.buildProcessor(vertx, message, op);
    if (processor != null) {
      vertx.<MessageResponse>executeBlocking(future -> {
        processor.process().setHandler(asyncResult -> {
          if (asyncResult.succeeded()) {
            future.complete(asyncResult.result());
          } else {
            future.fail(asyncResult.cause());
          }
        });
      }, event -> {
        finishResponse(message, event);
      });
    } else {
      LOGGER.warn("Invalid operation type");
      message.reply(new JsonObject(), new DeliveryOptions().addHeader(MessageConstants.MSG_OP_STATUS,
          MessageConstants.MSG_OP_STATUS_FAIL));
    }
  }

  @Override
  public void stop(Future<Void> stopFuture) throws Exception {
    // NOOP
    stopFuture.complete();
  }
  
  private void finishResponse(Message<JsonObject> message,
      AsyncResult<MessageResponse> asyncResult) {
    if (asyncResult.succeeded()) {
      message.reply(asyncResult.result().reply(), asyncResult.result().deliveryOptions());
    } else {
      LOGGER.warn("Failed to process command", asyncResult.cause());
      if (asyncResult.cause() instanceof HttpResponseWrapperException) {
        HttpResponseWrapperException exception = (HttpResponseWrapperException) asyncResult.cause();
        message.reply(new JsonObject().put(MessageConstants.MSG_HTTP_STATUS, exception.getStatus())
            .put(MessageConstants.MSG_HTTP_BODY, exception.getBody())
            .put(MessageConstants.MSG_HTTP_HEADERS, new JsonObject()));
      } else if (asyncResult.cause() instanceof MessageResponseWrapperException) {
        MessageResponseWrapperException exception =
            (MessageResponseWrapperException) asyncResult.cause();
        message.reply(exception.getMessageResponse().reply(),
            exception.getMessageResponse().deliveryOptions());
      } else {
        message.reply(new JsonObject().put(MessageConstants.MSG_HTTP_STATUS, 500)
            .put(MessageConstants.MSG_HTTP_BODY, new JsonObject())
            .put(MessageConstants.MSG_HTTP_HEADERS, new JsonObject()));
      }
    }
  }
}
