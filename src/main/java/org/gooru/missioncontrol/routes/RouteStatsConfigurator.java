
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

/**
 * @author szgooru Created On 01-Jul-2019
 */
public class RouteStatsConfigurator implements RouteConfigurator {

  private static final Logger LOGGER = LoggerFactory.getLogger(RouteStatsConfigurator.class);
  private EventBus eb = null;
  private long mbusTimeout;

  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
    this.eb = vertx.eventBus();
    this.mbusTimeout = config.getLong(MessageConstants.MBUS_TIMEOUT, 30L) * 1000;

    router.get(RouteConstants.API_STATS_BY_COUNTRY).handler(this::statsByCountries);
  }

  private void statsByCountries(RoutingContext routingContext) {
    RouteHandlerUtils.baseHandler(eb, routingContext, MessageConstants.MSG_OP_COUNTRIES_STATS,
        MessagebusEndpoints.MBEP_DISPATCHER, mbusTimeout, LOGGER);
  }

}
