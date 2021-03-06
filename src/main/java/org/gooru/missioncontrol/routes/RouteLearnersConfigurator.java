
package org.gooru.missioncontrol.routes;

import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.constants.MessagebusEndpoints;
import org.gooru.missioncontrol.constants.RouteConstants;
import org.gooru.missioncontrol.routes.utils.RouteHandlerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;


public class RouteLearnersConfigurator implements RouteConfigurator {

  private static final Logger LOGGER = LoggerFactory.getLogger(RouteLearnersConfigurator.class);
  private EventBus eb = null;
  private long mbusTimeout;

  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
    this.eb = vertx.eventBus();
    this.mbusTimeout = config.getLong(MessageConstants.MBUS_TIMEOUT, 30L) * 1000;

    router.get(RouteConstants.API_LEARNERS).handler(this::fetchLearners);
    router.get(RouteConstants.API_PERSONALIZED_LEARNERS).handler(this::fetchPersonalizedLearners);
  }

  private void fetchLearners(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(eb, routingContext, MessageConstants.MSG_OP_LEARNERS,
        MessagebusEndpoints.MBEP_DISPATCHER, mbusTimeout, LOGGER);
  }

  private void fetchPersonalizedLearners(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(eb, routingContext, MessageConstants.MSG_OP_PERSONALIZE_LEARNERS,
        MessagebusEndpoints.MBEP_DISPATCHER, mbusTimeout, LOGGER);
  }

}
