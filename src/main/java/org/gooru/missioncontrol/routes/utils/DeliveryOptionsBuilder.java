package org.gooru.missioncontrol.routes.utils;

import org.gooru.missioncontrol.constants.MessageConstants;
import org.gooru.missioncontrol.constants.RouteConstants;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.ext.web.RoutingContext;

/**
 * @author szgooru Created On 01-Jul-2019
 */
public final class DeliveryOptionsBuilder {
  private DeliveryOptionsBuilder() {
    throw new AssertionError();
  }

  public static DeliveryOptions buildWithApiVersion(RoutingContext context) {
    final String apiVersion = context.request().getParam(RouteConstants.VERSION);
    VersionValidatorUtility.validateVersion(apiVersion);
    return new DeliveryOptions().addHeader(MessageConstants.MSG_API_VERSION, apiVersion);
  }

}
