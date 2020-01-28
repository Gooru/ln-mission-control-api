package org.gooru.missioncontrol.processors.exceptions;


public class ActiveUserForTenantNotFoundException extends RuntimeException {

 
  private static final long serialVersionUID = 834109444994638188L;

  public ActiveUserForTenantNotFoundException() {}

  public ActiveUserForTenantNotFoundException(String message) {
    super(message);
  }
}
