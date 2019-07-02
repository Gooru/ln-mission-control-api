
package org.gooru.missioncontrol.processors.auth;

import org.gooru.missioncontrol.processors.responses.ExecutionResult;
import org.gooru.missioncontrol.processors.responses.MessageResponse;

/**
 * @author szgooru Created On 27-Jun-2019
 */
public interface Authorizer {

  ExecutionResult<MessageResponse> authorize(String op);
}
