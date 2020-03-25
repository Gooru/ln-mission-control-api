package org.gooru.missioncontrol.processors.catalog.apis;

import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.processors.MessageProcessor;
import org.gooru.missioncontrol.processors.responses.MessageResponse;
import org.gooru.missioncontrol.processors.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;


public class CatalogTranscriptsProcessor extends CatalogProccessor implements MessageProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(CatalogTranscriptsProcessor.class);

  
  public CatalogTranscriptsProcessor(Vertx vertx, Message<JsonObject> message) {
    super(vertx, message);
  }
 
  @Override
  public Future<MessageResponse> process() {
    try {
      fetchTranscripts();
    } catch (Throwable throwable) {
      LOGGER.warn("Encountered exception", throwable);
      result.fail(throwable);
    }
    return result;
  }

  private void fetchTranscripts() {
    try {
      JsonObject requestBody = message.body().getJsonObject(MessageConstants.MSG_HTTP_BODY);
      String resourceIds = validateAndExtractRequest(requestBody);

       this.catalogAPIService.fetchTranscripts(resourceIds).setHandler(asyncResult->{
         if(asyncResult.succeeded()) {
           result.complete(MessageResponseFactory.createGetResponse(new JsonObject(asyncResult.result())));
         }
         else {
           LOGGER.debug("Failed to fetch transcripts..", asyncResult.cause());
           result.fail(asyncResult.cause());
         }
       });
    } catch (DecodeException e) {
      LOGGER.warn("Not able to convert data to JSON", e);
      result.fail(e);
    } catch (Throwable throwable) {
      LOGGER.warn("Encountered exception", throwable);
      result.fail(throwable);
    }
  }
  

}
