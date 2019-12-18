
package org.gooru.missioncontrol.routes;

import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.constants.RouteConstants;
import org.gooru.missioncontrol.processors.groups.create.GroupsDataUploadHandler;
import org.gooru.missioncontrol.processors.responses.MessageResponse;
import org.gooru.missioncontrol.routes.utils.RouteRequestUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

/**
 * @author szgooru Created On 31-Aug-2019
 */
public class RouteGroupsConfigurator implements RouteConfigurator {

  private static final Logger LOGGER = LoggerFactory.getLogger(RouteGroupsConfigurator.class);

  private EventBus eb = null;
  private long mbusTimeout;
  
  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
    this.eb = vertx.eventBus();
    this.mbusTimeout = config.getLong(MessageConstants.MBUS_TIMEOUT, 30L) * 1000;
    
    router.post(RouteConstants.API_GROUPS)
    .handler(routingContext -> vertx.<MessageResponse>executeBlocking(future -> {
      JsonObject session = (JsonObject) routingContext.get(MessageConstants.MSG_KEY_SESSION);
      LOGGER.debug("session received: {}", session.toString());
        new GroupsDataUploadHandler(routingContext, session).upload().setHandler(asyncResult -> {
          if (asyncResult.succeeded()) {
            future.complete(asyncResult.result());
          } else {
            future.fail(asyncResult.cause());
          }
        });
    }, result -> {
      MessageResponse res = (MessageResponse) result.result();
      JsonObject resJson = res.reply();
      LOGGER.debug("reply: {}", res.reply());
      HttpServerResponse response = routingContext.response();
      response.setStatusCode(resJson.getInteger(MessageConstants.MSG_HTTP_STATUS));
       //TODO: set response body
       response.end();
    }));
  }

  
}
