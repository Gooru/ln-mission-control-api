package org.gooru.missioncontrol.processors.exceptions;

/**
 * @author ashish on 7/3/18.
 */
public class ActiveTenantNotFoundException extends RuntimeException {

  public ActiveTenantNotFoundException() {}

  public ActiveTenantNotFoundException(String message) {
    super(message);
  }
}
