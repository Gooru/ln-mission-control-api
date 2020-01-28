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
  private static final String DISPATCHER_VERTICLE = "org.gooru.missioncontrol.bootstrap.verticles.DispatcherVerticle";
  private static final String SESSION_PERSISTENCE_VERTICLE = "org.gooru.missioncontrol.bootstrap.verticles.SessionPersistenceVerticle";

  private final Iterator<String> iterator;

  public VerticleRegistry() {
    List<String> initializers = new ArrayList<>();
    initializers.add(AUTH_VERTICLE);
    initializers.add(HTTP_VERTICLE);
    initializers.add(DISPATCHER_VERTICLE);
    initializers.add(SESSION_PERSISTENCE_VERTICLE);
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
