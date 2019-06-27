
package org.gooru.missioncontrol.bootstrap.verticles;

import org.gooru.missioncontrol.constants.MessagebusEndpoints;
import org.gooru.missioncontrol.processors.ProcessorBuilder;
import org.gooru.missioncontrol.processors.responses.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;

/**
 * @author szgooru Created On 27-Jun-2019
 */
public class DispatcherVerticle extends AbstractVerticle {

  private final static Logger LOGGER = LoggerFactory.getLogger(DispatcherVerticle.class);

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    EventBus eb = vertx.eventBus();
    eb.localConsumer(MessagebusEndpoints.MBEP_DISPATCHER, message -> {
      LOGGER.info("received message with body: {}", message.body());

      vertx.executeBlocking(future -> {
        MessageResponse result = ProcessorBuilder.build(message).process();
        future.complete(result);
      }, res -> {
        MessageResponse result = (MessageResponse) res.result();
        LOGGER.debug("sending response :{}", result.reply());
        message.reply(result.reply(), result.deliveryOptions());
      });
    }).completionHandler(result -> {
      if (result.succeeded()) {
        LOGGER.info("Tracker end point ready to listen");
        startFuture.complete();
      } else {
        LOGGER.error("Error registering the Tracker handler. Halting the machinery");
        startFuture.fail(result.cause());
        Runtime.getRuntime().halt(1);
      }
    });
  }

  @Override
  public void stop(Future<Void> stopFuture) throws Exception {
    // NOOP
    stopFuture.complete();
  }

}
