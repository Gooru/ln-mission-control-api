
package org.gooru.missioncontrol.processors.auth;

import org.gooru.missioncontrol.processors.responses.ExecutionResult;
import org.gooru.missioncontrol.processors.responses.MessageResponse;

/**
 * @author szgooru Created On 27-Jun-2019
 */
public class UserPermissionAuthorizer implements Authorizer {

  
  public UserPermissionAuthorizer() {
  }

  @Override
  public ExecutionResult<MessageResponse> authorize(String op) {
    return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
  }

}
