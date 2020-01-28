package org.gooru.missioncontrol.processors.exceptions;


public class ActiveTenantNotFoundException extends RuntimeException {

 
  private static final long serialVersionUID = 5124665220049314670L;

  public ActiveTenantNotFoundException() {}

  public ActiveTenantNotFoundException(String message) {
    super(message);
  }
}
