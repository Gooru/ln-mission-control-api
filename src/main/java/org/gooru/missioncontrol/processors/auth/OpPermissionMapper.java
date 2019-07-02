
package org.gooru.missioncontrol.processors.auth;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author szgooru Created On 28-Jun-2019
 */
public final class OpPermissionMapper {

  private static final Map<String, List<String>> opPermissionMapping;

  private OpPermissionMapper() {
    throw new AssertionError();
  }

  static {
    opPermissionMapping = initializeMapping();
  }

  private static Map<String, List<String>> initializeMapping() {
    Map<String, List<String>> mapping = new HashMap<String, List<String>>();
    return Collections.unmodifiableMap(mapping);
  }

  public static List<String> fetchPermissions(String op) {
    return opPermissionMapping.get(op);
  }
}
