package org.gooru.missioncontrol.processors.exceptions;

/**
 * @author ashish on 7/3/18.
 */
public class ActiveUserForTenantNotFoundException extends RuntimeException {

  public ActiveUserForTenantNotFoundException() {}

  public ActiveUserForTenantNotFoundException(String message) {
    super(message);
  }
}
