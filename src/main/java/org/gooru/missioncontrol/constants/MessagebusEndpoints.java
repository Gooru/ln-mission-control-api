
package org.gooru.missioncontrol.constants;

/**
 * @author szgooru Created On 19-Jun-2019
 */
public final class MessagebusEndpoints {
  public static final String MBEP_AUTH = "org.gooru.missioncontrol.message.bus.auth";
  public static final String MBEP_DISPATCHER = "org.gooru.missioncontrol.message.bus.dispatcher";
  public static final String MBEP_SESSION_PERSISTENCE =
      "org.gooru.missioncontrol.message.session.persistence";

  private MessagebusEndpoints() {
    throw new AssertionError();
  }
}
