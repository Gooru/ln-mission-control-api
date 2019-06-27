package org.gooru.missioncontrol.processors.exceptions;

import org.gooru.missioncontrol.constants.HttpConstants;
import org.gooru.missioncontrol.constants.MessageConstants;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 27-Jun-2019
 */
public final class HttpResponseWrapperException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final HttpConstants.HttpStatus status;
  private final JsonObject payload;

  public HttpResponseWrapperException(HttpConstants.HttpStatus status, JsonObject payload) {
    this.status = status;
    this.payload = payload;
  }

  public HttpResponseWrapperException(HttpConstants.HttpStatus status, String message) {
    super(message);
    this.status = status;
    this.payload = new JsonObject().put(MessageConstants.MSG_MESSAGE, message);
  }

  public int getStatus() {
    return this.status.getCode();
  }

  public JsonObject getBody() {
    return this.payload;
  }
}
