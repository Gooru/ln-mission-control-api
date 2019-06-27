
package org.gooru.missioncontrol.routes;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

/**
 * @author szgooru Created On 27-Jun-2019
 */
public interface RouteConfigurator {
  void configureRoutes(Vertx vertx, Router router, JsonObject config);
}
