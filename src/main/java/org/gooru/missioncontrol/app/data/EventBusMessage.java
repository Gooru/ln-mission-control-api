
package org.gooru.missioncontrol.app.data;

import java.util.UUID;
import org.gooru.missioncontrol.constants.MessageConstants;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 02-Jul-2019
 */
public class EventBusMessage {
  private final String sessionToken;
  private final JsonObject requestBody;
  private final UUID userId;
  private final JsonObject session;
  private final JsonObject headers;

  public String getSessionToken() {
    return sessionToken;
  }

  public JsonObject getRequestBody() {
    return requestBody;
  }
  
  public JsonObject getRequestHeaders() {
    return headers;
  }

  public UUID getUserId() {
    return userId;
  }

  public JsonObject getSession() {
    return session;
  }

  private EventBusMessage(String sessionToken, JsonObject requestBody, UUID userId,
      JsonObject session, JsonObject headers) {
    this.sessionToken = sessionToken;
    this.requestBody = requestBody;
    this.userId = userId;
    this.session = session;
    this.headers = headers;
  }

  public static EventBusMessage eventBusMessageBuilder(Message<JsonObject> message) {
    new JsonObject();
    String sessionToken = message.body().getString(MessageConstants.MSG_HEADER_TOKEN);
    String userId = message.body().getString(MessageConstants.MSG_USER_ID, null);
    JsonObject requestBody = message.body().getJsonObject(MessageConstants.MSG_HTTP_BODY);
    JsonObject session = message.body().getJsonObject(MessageConstants.MSG_KEY_SESSION);
    JsonObject headers = getHeaders(message.headers());
    if (userId != null) {
      return new EventBusMessage(sessionToken, requestBody, UUID.fromString(userId), session, headers);
    } else {
      return new EventBusMessage(sessionToken, requestBody, null, session, headers);
    }
  }

  private static JsonObject getHeaders(MultiMap headers) {
    JsonObject headersAsJsonObject = new JsonObject();
    if (headers != null) {
      headers.forEach((header) -> {
        headersAsJsonObject.put(header.getKey(), header.getValue());
      });
    }
    return headersAsJsonObject;
  }
}
