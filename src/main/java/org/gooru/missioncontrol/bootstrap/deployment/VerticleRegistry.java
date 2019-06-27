package org.gooru.missioncontrol.bootstrap.deployment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author szgooru Created On 19-Jun-2019
 */
public class VerticleRegistry implements Iterable<String> {

  private static final String AUTH_VERTICLE = "org.gooru.missioncontrol.bootstrap.verticles.AuthVerticle";
  private static final String HTTP_VERTICLE = "org.gooru.missioncontrol.bootstrap.verticles.HttpVerticle";

  private final Iterator<String> iterator;

  public VerticleRegistry() {
    List<String> initializers = new ArrayList<>();
    initializers.add(AUTH_VERTICLE);
    initializers.add(HTTP_VERTICLE);
    iterator = initializers.iterator();
  }

  @Override
  public Iterator<String> iterator() {
    return new Iterator<String>() {

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public String next() {
        return iterator.next();
      }
    };
  }
}
