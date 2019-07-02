
package org.gooru.missioncontrol.processors;

import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.processors.partners.FetchPartnersProcessor;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 01-Jul-2019
 */
public final class MessageProcessorBuilder {

  private MessageProcessorBuilder() {
    throw new AssertionError();
  }

  public static MessageProcessor buildProcessor(Vertx vertx, Message<JsonObject> message,
      String op) {
    switch (op) {
      case MessageConstants.MSG_OP_PARTNERS_GET:
        return new FetchPartnersProcessor(vertx, message);
      case MessageConstants.MSG_OP_PARTNER_GET:
        return null;
      case MessageConstants.MSG_OP_COUNTRIES_STATS:
        return null;

      default:
        return null;
    }
  }
}
