package org.gooru.missioncontrol.routes.responses.transformers;

import java.util.Map;
import io.vertx.core.json.JsonObject;

/**
 * @author ashish on 10/1/18.
 */
public interface ResponseTransformer {

  void transform();

  JsonObject transformedBody();

  Map<String, String> transformedHeaders();

  int transformedStatus();

}
