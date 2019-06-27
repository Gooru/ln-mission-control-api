package org.gooru.missioncontrol.routes;

import org.gooru.missioncontrol.constants.MessageConstants;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * @author szgooru Created On 27-Jun-2019
 */
final class RouteGlobalConfigurator implements RouteConfigurator {

  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
    final long maxSizeInMb = config.getLong(MessageConstants.MAX_REQ_BODY_SIZE, 5L);
    BodyHandler bodyHandler = BodyHandler.create().setBodyLimit(maxSizeInMb * 1024 * 1024);
    router.route().handler(bodyHandler);
  }
}
