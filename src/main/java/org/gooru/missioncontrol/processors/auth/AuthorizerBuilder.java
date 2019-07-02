
package org.gooru.missioncontrol.processors.auth;

/**
 * @author szgooru Created On 28-Jun-2019
 */
public final class AuthorizerBuilder {

  private AuthorizerBuilder() {
    throw new AssertionError();
  }

  public static Authorizer buildUserPermissionAuthorizer() {
    return new UserPermissionAuthorizer();
  }
}
