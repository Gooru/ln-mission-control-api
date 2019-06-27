
package org.gooru.missioncontrol.constants;

/**
 * @author szgooru Created On 27-Jun-2019
 */
public final class RouteConstants {
  
  private static final String API_VERSION = "v1";
  private static final String API_BASE_ROUTE = "/api/missioncontrol/" + API_VERSION + '/';
  
  public static final String API_AUTH_ROUTE = API_BASE_ROUTE + '*';
  
  private RouteConstants() {
    throw new AssertionError();
  }
}
