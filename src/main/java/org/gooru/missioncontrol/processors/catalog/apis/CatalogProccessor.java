package org.gooru.missioncontrol.processors.catalog.apis;

import org.gooru.missioncontrol.constants.HttpConstants;
import org.gooru.missioncontrol.processors.exceptions.HttpResponseWrapperException;
import org.gooru.missioncontrol.processors.responses.MessageResponse;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class CatalogProccessor {
  
  protected final Vertx vertx;
  protected final Message<JsonObject> message;
  protected final Future<MessageResponse> result;
  protected final CatalogAPIService catalogAPIService; 
  protected static final String RESOURCE_ID = "resource_id";

  
  protected CatalogProccessor(Vertx vertx, Message<JsonObject> message) {
    this.vertx = vertx;
    this.message = message;
    this.result = Future.future();
    this.catalogAPIService = new CatalogAPIService(this.vertx.createHttpClient());
  }
  
 
  protected String validateAndExtractRequest(JsonObject requestData) {
      String resourceId = requestData.getString(RESOURCE_ID);
      
      if(resourceId == null || resourceId.isEmpty()) {
        throw new HttpResponseWrapperException(HttpConstants.HttpStatus.BAD_REQUEST,
          "Invalid resource id in payload");
      }
      
      return resourceId;        
  }

 
}
