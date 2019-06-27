package org.gooru.missioncontrol.processors.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import org.gooru.missioncontrol.processors.ProcessorContext;
import io.vertx.core.json.JsonArray;

public final class HelperUtility {

  private HelperUtility() {
    throw new AssertionError();
  }

  public static boolean validateUUID(String id) {
    try {
      UUID.fromString(id);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public static String toPostgresArrayString(Collection<String> input) {
    int approxSize = ((input.size() + 1) * 36); // Length of UUID is around 36 chars
    Iterator<String> it = input.iterator();
    if (!it.hasNext()) {
      return "{}";
    }

    StringBuilder sb = new StringBuilder(approxSize);
    sb.append('{');
    for (; ; ) {
      String s = it.next();
      sb.append('"').append(s).append('"');
      if (!it.hasNext()) {
        return sb.append('}').toString();
      }
      sb.append(',');
    }
  }

  public static String toPostgresArrayInt(Collection<Integer> input) {
    Iterator<Integer> it = input.iterator();
    if (!it.hasNext()) {
      return "{}";
    }

    StringBuilder sb = new StringBuilder();
    sb.append('{');
    for (; ; ) {
      Integer i = it.next();
      sb.append(i);
      if (!it.hasNext()) {
        return sb.append('}').toString();
      }
      sb.append(',');
    }
  }

  public static String readRequestParam(String param, ProcessorContext context) {
    JsonArray requestParams = context.request().getJsonArray(param);
    if (requestParams == null || requestParams.isEmpty()) {
      return null;
    }

    String value = requestParams.getString(0);
    return (value != null && !value.isEmpty()) ? value : null;
  }
}
