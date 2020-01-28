package org.gooru.missioncontrol.app.data;

import com.fasterxml.jackson.databind.JsonNode;


public class JsonNodeHolder {
  private final JsonNode jsonNode;

  public JsonNodeHolder(JsonNode jsonNode) {
    this.jsonNode = jsonNode;
  }

  public JsonNode getJsonNode() {
    return jsonNode;
  }
}
