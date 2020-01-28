package org.gooru.missioncontrol.routes;

import org.gooru.missioncontrol.constants.HttpConstants;
import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.constants.MessagebusEndpoints;
import org.gooru.missioncontrol.constants.RouteConstants;
import org.gooru.missioncontrol.routes.utils.DeliveryOptionsBuilder;
import org.gooru.missioncontrol.routes.utils.RouteHandlerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

class RouteAuthenticationConfigurator implements RouteConfigurator {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(RouteAuthenticationConfigurator.class);

  private EventBus eb = null;
  private long mbusTimeout;

  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
    eb = vertx.eventBus();
    mbusTimeout = config.getLong(MessageConstants.MBUS_TIMEOUT, 30L) * 1000;
    router.post(RouteConstants.API_AUTH_SIGNIN).handler(this::signInUser);
  }

  private void signInUser(RoutingContext routingContext) {
    HttpServerRequest request = routingContext.request();
    String authorization = request.getHeader(HttpConstants.HEADER_AUTH);
    DeliveryOptions options =
        DeliveryOptionsBuilder.buildWithApiVersion(routingContext).setSendTimeout(mbusTimeout);
    String host = request.getHeader(HttpConstants.HEADER_HOST);
    String referer = request.getHeader(HttpConstants.HEADER_REFERER);
    if (host != null) {
      options.addHeader(MessageConstants.MSG_HEADER_REQUEST_DOMAIN, host);
    } else if (referer != null) {
      options.addHeader(MessageConstants.MSG_HEADER_REQUEST_DOMAIN, referer);
    }
    if (authorization != null && authorization.startsWith(HttpConstants.BASIC)) {
      String basicAuthCredentials = authorization.substring(HttpConstants.BASIC.length()).trim();
      options.addHeader(MessageConstants.MSG_HEADER_BASIC_AUTH, basicAuthCredentials);
    }
    RouteHandlerUtils.baseHandler(eb, routingContext, MessageConstants.MSG_OP_USER_SIGNIN,
        MessagebusEndpoints.MBEP_DISPATCHER, mbusTimeout, LOGGER, options);
  }


}
