package org.gooru.missioncontrol.bootstrap.startup;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 19-Jun-2019
 */
public interface Initializer {

  void initializeComponent(Vertx vertx, JsonObject config);

}
