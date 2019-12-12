
package org.gooru.missioncontrol.processors;

import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.processors.learners.FetchLearnersProcessor;
import org.gooru.missioncontrol.processors.learners.personalized.FetchPersonalizedLearnersModelMapper;
import org.gooru.missioncontrol.processors.learners.personalized.FetchPersonalizedLearnersProcessor;
import org.gooru.missioncontrol.processors.partners.FetchPartnerProcessor;
import org.gooru.missioncontrol.processors.partners.FetchPartnersProcessor;
import org.gooru.missioncontrol.processors.stats.countries.StatsByCountryProcessor;
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
        return new FetchPartnerProcessor(vertx, message);
      case MessageConstants.MSG_OP_COUNTRIES_STATS:
        return new StatsByCountryProcessor(vertx, message);
      case MessageConstants.MSG_OP_LEARNERS:
        return new FetchLearnersProcessor(vertx, message);
      case MessageConstants.MSG_OP_PERSONALIZE_LEARNERS:
        return new FetchPersonalizedLearnersProcessor(vertx, message);
      
      default:
        return null;
    }
  }
}
