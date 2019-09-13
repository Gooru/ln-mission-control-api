
package org.gooru.missioncontrol.routes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author szgooru Created On 27-Jun-2019
 */
public class RouteConfiguration implements Iterable<RouteConfigurator> {

  private final Iterator<RouteConfigurator> iterator;

  public RouteConfiguration() {
    List<RouteConfigurator> configurators = new ArrayList<>(32);
    // First the global handler to enable to body reading etc
    configurators.add(new RouteGlobalConfigurator());

    // For rest of handlers, Auth should always be first one
    configurators.add(new RouteAuthConfigurator());
    configurators.add(new RoutePartnerAPIConfigurator());
    configurators.add(new RouteStatsConfigurator());
    configurators.add(new RouteGroupsConfigurator());
    configurators.add(new RouteFailureConfigurator());

    iterator = configurators.iterator();
  }

  @Override
  public Iterator<RouteConfigurator> iterator() {
    return new Iterator<RouteConfigurator>() {

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public RouteConfigurator next() {
        return iterator.next();
      }
    };
  }
}
