
package org.gooru.missioncontrol.processors;

import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.processors.auth.signinuser.SignInUserProcessor;
import org.gooru.missioncontrol.processors.catalog.apis.CatalogTranscriptsProcessor;
import org.gooru.missioncontrol.processors.catalog.apis.CatalogTranscriptsSummaryProcessor;
import org.gooru.missioncontrol.processors.learners.FetchLearnersProcessor;
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
      case MessageConstants.MSG_OP_USER_SIGNIN:
        return new SignInUserProcessor(vertx, message);
      case MessageConstants.MSG_OP_TRANSCRIPTS:
        return new CatalogTranscriptsProcessor(vertx, message);
      case MessageConstants.MSG_OP_SUMMARY:
        return new CatalogTranscriptsSummaryProcessor(vertx, message);  
      default:
        return null;
    }
  }
}
