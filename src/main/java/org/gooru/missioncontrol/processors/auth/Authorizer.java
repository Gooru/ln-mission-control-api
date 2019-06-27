
package org.gooru.missioncontrol.processors.auth;

import org.gooru.missioncontrol.processors.responses.MessageResponse;

/**
 * @author szgooru Created On 27-Jun-2019
 */
public interface Authorizer {

  MessageResponse authorize(String op);
}
