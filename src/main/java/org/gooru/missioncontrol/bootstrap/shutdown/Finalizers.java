package org.gooru.missioncontrol.bootstrap.shutdown;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.gooru.missioncontrol.bootstrap.component.DataSourceRegistry;

public class Finalizers implements Iterable<Finalizer> {

  private final Iterator<Finalizer> iterator;

  public Finalizers() {
    List<Finalizer> finalizers = new ArrayList<>();
    finalizers.add(DataSourceRegistry.getInstance());
    iterator = finalizers.iterator();
  }

  @Override
  public Iterator<Finalizer> iterator() {
    return new Iterator<Finalizer>() {

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public Finalizer next() {
        return iterator.next();
      }
    };
  }
}
