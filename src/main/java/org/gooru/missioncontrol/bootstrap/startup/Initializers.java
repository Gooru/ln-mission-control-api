package org.gooru.missioncontrol.bootstrap.startup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.gooru.missioncontrol.bootstrap.component.AppConfiguration;
import org.gooru.missioncontrol.bootstrap.component.DataSourceRegistry;

/**
 * @author szgooru Created On 19-Jun-2019
 */
public class Initializers implements Iterable<Initializer> {

  private final Iterator<Initializer> iterator;

  public Initializers() {
    List<Initializer> initializers = new ArrayList<>();
    initializers.add(DataSourceRegistry.getInstance());
    initializers.add(AppConfiguration.getInstance());
    iterator = initializers.iterator();
  }

  @Override
  public Iterator<Initializer> iterator() {
    return new Iterator<Initializer>() {

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public Initializer next() {
        return iterator.next();
      }
    };
  }
}
