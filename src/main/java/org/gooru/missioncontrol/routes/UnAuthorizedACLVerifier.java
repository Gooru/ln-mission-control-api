package org.gooru.missioncontrol.routes;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.gooru.missioncontrol.constants.RouteConstants;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;

final class UnAuthorizedACLVerifier {

  private UnAuthorizedACLVerifier() {
    throw new AssertionError();
  }

  private static final Map<String, List<String>> ROUTES;

  /**
   * This mapper constant have route suffix url and it's request methods which are allowed by
   * gateway for unauthorized user.
   */
  static {
    Map<String, List<String>> routes = new HashMap<>();
    routes.put(RouteConstants.SIGNIN, Arrays.asList(HttpMethod.POST.name()));
    ROUTES = Collections.unmodifiableMap(routes);
  }

  static boolean hasPermit(HttpServerRequest httpServerRequest) {
    return permittedBasedOnRouteAndHttpMethod(httpServerRequest);
  }

  private static boolean permittedBasedOnRouteAndHttpMethod(HttpServerRequest httpServerRequest) {
    final String path = httpServerRequest.path();
    for (Entry<String, List<String>> entry : ROUTES.entrySet()) {
      if (path.endsWith(entry.getKey())
          && entry.getValue().contains(httpServerRequest.method().name())) {
        return true;
      }
    }
    return false;
  }

}
