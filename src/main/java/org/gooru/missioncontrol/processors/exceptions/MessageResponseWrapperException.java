package org.gooru.missioncontrol.processors.exceptions;

import org.gooru.missioncontrol.processors.responses.MessageResponse;

/**
 * @author szgooru Created On 27-Jun-2019
 */
public class MessageResponseWrapperException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final MessageResponse response;

  public MessageResponseWrapperException(MessageResponse response) {
    this.response = response;
  }

  public MessageResponse getMessageResponse() {
    return this.response;
  }
}
