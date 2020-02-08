
package org.gooru.missioncontrol.constants;

/**
 * @author szgooru Created On 27-Jun-2019
 */
public final class RouteConstants {

  private static final String SEP = "/";
  private static final String COLON = ":";

  private static final String API_VERSION = ":version";
  private static final String API_BASE_ROUTE = "/api/missioncontrol/" + API_VERSION + '/';

  public static final String API_AUTH_ROUTE = API_BASE_ROUTE + '*';

  private static final String OP_PARTNERS = "partners";
  private static final String OP_GROUPS = "groups";

  public static final String VERSION = "version";
  public static final String ID_PARTNER = "partnerId";
  public static final String SIGNIN = "signin";
  public static final String AUTH = "auth";

  // api/missioncontrol/v1/partners
  public static final String API_PARTNERS_GET = API_BASE_ROUTE + OP_PARTNERS;

  // api/missioncontrol/v1/partners/{partnerId}
  public static final String API_PARTNER_GET =
      API_BASE_ROUTE + OP_PARTNERS + SEP + COLON + ID_PARTNER;

  // api/missioncontrol/v1/stats/countries
  public static final String API_STATS_BY_COUNTRY = API_BASE_ROUTE + "stats/countries";

  // Groups creation APIs
  // api/missioncontrol/v1/groups
  public static final String API_GROUPS = API_BASE_ROUTE + OP_GROUPS;

  // api/missioncontrol/v1/learners
  public static final String API_LEARNERS = API_BASE_ROUTE + "learners";


  // api/missioncontrol/v1/auth/signin
  public static final String API_AUTH_SIGNIN = API_BASE_ROUTE + AUTH + SEP + SIGNIN;



  private RouteConstants() {
    throw new AssertionError();
  }
}
