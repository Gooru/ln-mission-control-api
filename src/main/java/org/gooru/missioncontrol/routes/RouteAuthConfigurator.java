
package org.gooru.missioncontrol.routes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.gooru.missioncontrol.constants.HttpConstants;
import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.constants.MessagebusEndpoints;
import org.gooru.missioncontrol.constants.RouteConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @author szgooru Created On 27-Jun-2019
 */
public class RouteAuthConfigurator implements RouteConfigurator {

  private static final Logger LOGGER = LoggerFactory.getLogger(RouteAuthConfigurator.class);
  private static final String HEADER_AUTH_PREFIX = "Token";

  private static final Pattern AUTH_PATTERN = Pattern.compile('^' + HEADER_AUTH_PREFIX
      + "[\\s]+((?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?)\\s*$");

  private EventBus eb = null;
  private long mbusTimeout;

  @Override
  public void configureRoutes(Vertx vertx, Router router, JsonObject config) {
    this.eb = vertx.eventBus();
    this.mbusTimeout = config.getLong(MessageConstants.MBUS_TIMEOUT, 30L);

    router.route(RouteConstants.API_AUTH_ROUTE).handler(this::handleAuth);
  }

  private void handleAuth(RoutingContext routingContext) {
    String authToken =
        extractSessionToken(routingContext.request().getHeader(HttpConstants.HEADER_AUTH));
    if (authToken == null || authToken.isEmpty()) {
      routingContext.response().setStatusCode(HttpConstants.HttpStatus.UNAUTHORIZED.getCode())
          .setStatusMessage(HttpConstants.HttpStatus.UNAUTHORIZED.getMessage()).end();
    } else {
      // If the auth token is present, we send it to Message Bus for validation. We stash it on to
      // routing context for good measure. We could have done that later in success callback but we
      // want to avoid closure from callback for success to this local context, hence it is here
      routingContext.put(MessageConstants.MSG_HEADER_TOKEN, authToken);
      DeliveryOptions options = new DeliveryOptions().setSendTimeout(mbusTimeout * 1000)
          .addHeader(MessageConstants.MSG_HEADER_OP, MessageConstants.MSG_OP_API_AUTH)
          .addHeader(MessageConstants.MSG_HEADER_TOKEN, authToken);
      eb.send(MessagebusEndpoints.MBEP_AUTH, new JsonObject(), options, reply -> {
        if (reply.succeeded()) {
          AuthResponseParser responseParser = new AuthResponseParser(reply.result());
          if (responseParser.isAuthorized()) {
            routingContext.put(MessageConstants.MSG_HEADER_CLIENTID, responseParser.clientId());
            routingContext.put(MessageConstants.MSG_HEADER_TENANTID, responseParser.tenantId());
            routingContext.next();
          } else {
            routingContext.response().setStatusCode(HttpConstants.HttpStatus.UNAUTHORIZED.getCode())
                .setStatusMessage(HttpConstants.HttpStatus.UNAUTHORIZED.getMessage()).end();
          }
        } else {
          LOGGER.error("Not able to send message", reply.cause());
          routingContext.response().setStatusCode(HttpConstants.HttpStatus.ERROR.getCode()).end();
        }
      });
    }
  }

  private static String extractSessionToken(String authHeader) {
    if (authHeader == null || authHeader.isEmpty()) {
      LOGGER.debug("auth token is null or empty");
      return null;
    }
    Matcher authMatcher = AUTH_PATTERN.matcher(authHeader);
    if (authMatcher.matches()) {
      return authMatcher.group(1);
    }
    LOGGER.debug("incorrect format of the auth token '{}'", authHeader);
    return null;
  }

  private static class AuthResponseParser {
    private final boolean isAuthorized;
    private final Message<Object> message;

    public AuthResponseParser(Message<Object> message) {
      this.message = message;
      if (message != null) {
        LOGGER.debug("received message with body: {}", message.body().toString());
        if (!(message.body() instanceof JsonObject)) {
          LOGGER.error("message body should be of type json");
          throw new IllegalArgumentException("message body should be of type json");
        }
        String result = message.headers().get(MessageConstants.MSG_OP_STATUS);
        LOGGER.debug("Received header from Auth response : {}", result);
        isAuthorized =
            result != null && result.equalsIgnoreCase(MessageConstants.MSG_OP_STATUS_SUCCESS);
      } else {
        isAuthorized = false;
      }
    }

    public boolean isAuthorized() {
      return isAuthorized;
    }

    public String clientId() {
      if (!isAuthorized) {
        return null;
      }
      JsonObject messageBody = (JsonObject) message.body();
      return messageBody.getJsonObject(MessageConstants.MSG_HTTP_BODY)
          .getJsonObject(MessageConstants.MSG_HTTP_RESPONSE)
          .getString(MessageConstants.MSG_HEADER_CLIENTID);
    }

    public String tenantId() {
      if (!isAuthorized) {
        return null;
      }
      JsonObject messageBody = (JsonObject) message.body();
      return messageBody.getJsonObject(MessageConstants.MSG_HTTP_BODY)
          .getJsonObject(MessageConstants.MSG_HTTP_RESPONSE)
          .getString(MessageConstants.MSG_HEADER_TENANTID);
    }
  }
}
