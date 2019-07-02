
package org.gooru.missioncontrol.processors.partners;

import org.gooru.missioncontrol.bootstrap.component.jdbi.DBICreator;
import org.gooru.missioncontrol.processors.MessageProcessor;
import org.gooru.missioncontrol.processors.responses.MessageResponse;
import org.gooru.missioncontrol.processors.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 01-Jul-2019
 */
public class FetchPartnersProcessor implements MessageProcessor {

  private final static Logger LOGGER = LoggerFactory.getLogger(FetchPartnersProcessor.class);

  private final static PartnersService PARTNERS_SERVICE =
      new PartnersService(DBICreator.getDbiForDefaultDS());

  private final Vertx vertx;
  private final Message<JsonObject> message;
  private final Future<MessageResponse> result;

  public FetchPartnersProcessor(Vertx vertx, Message<JsonObject> message) {
    this.vertx = vertx;
    this.message = message;
    this.result = Future.future();
  }

  @Override
  public Future<MessageResponse> process() {
    JsonObject response = new JsonObject().put("OK", "tested");
    result.complete(MessageResponseFactory.createGetResponse(response));
    return result;
  }

}
