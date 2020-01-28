
package org.gooru.missioncontrol.routes.utils;

import org.gooru.missioncontrol.constants.MessageConstants;
import org.slf4j.Logger;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * @author szgooru Created On 01-Jul-2019
 */
public class RouteHandlerUtils {
  public static void baseHandler(EventBus eb, RoutingContext routingContext, String op,
      String eventBusEndPoint, long mbusTimeout, Logger logger) {

    DeliveryOptions options = DeliveryOptionsBuilder.buildWithApiVersion(routingContext)
        .setSendTimeout(mbusTimeout).addHeader(MessageConstants.MSG_HEADER_OP, op);
    eb.<JsonObject>send(eventBusEndPoint,
        RouteRequestUtility.getBodyForMessage(routingContext, true), options,
        reply -> RouteResponseUtility.responseHandler(routingContext, reply, logger));
  }

  public static void baseHandler(EventBus eb, RoutingContext routingContext, String op,
      String eventBusEndPoint, long mbusTimeout, Logger logger, DeliveryOptions options) {
    DeliveryOptionsBuilder.buildWithApiVersion(routingContext, options).setSendTimeout(mbusTimeout)
        .addHeader(MessageConstants.MSG_HEADER_OP, op);
    eb.<JsonObject>send(eventBusEndPoint,
        RouteRequestUtility.getBodyForMessage(routingContext, true), options,
        reply -> RouteResponseUtility.responseHandler(routingContext, reply, logger));
  }
}
