package org.gooru.missioncontrol.routes;

import org.gooru.missioncontrol.constants.HttpConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @author szgooru Created On 27-Jun-2019
 */
public class RouteFailureConfigurator implements RouteConfigurator {

  private static final Logger LOGGER = LoggerFactory.getLogger(RouteFailureConfigurator.class);
  public static final String CAUGHT_UNREGISTERED_EXCEPTION_WILL_SEND_HTTP_500 =
      "Caught unregistered exception, will send HTTP.500";

  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
    router.put().failureHandler(this::handleFailures);
    router.post().failureHandler(this::handleFailures);
  }

  private void handleFailures(RoutingContext frc) {
    Throwable currentThrowable = frc.failure();
    if (currentThrowable instanceof io.vertx.core.json.DecodeException) {
      LOGGER.error("Caught registered exception", currentThrowable);
      frc.response().setStatusCode(HttpConstants.HttpStatus.BAD_REQUEST.getCode())
          .end("invalid json payload");
    } else {
      LOGGER.error(CAUGHT_UNREGISTERED_EXCEPTION_WILL_SEND_HTTP_500, currentThrowable);
      frc.response().setStatusCode(HttpConstants.HttpStatus.ERROR.getCode()).end("internal error");
    }
  }
}
