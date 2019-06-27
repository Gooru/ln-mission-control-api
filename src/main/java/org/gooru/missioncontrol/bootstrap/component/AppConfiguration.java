package org.gooru.missioncontrol.bootstrap.component;

import org.gooru.missioncontrol.bootstrap.startup.Initializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 19-Jun-2019
 */
public final class AppConfiguration implements Initializer {
  private static final String APP_CONFIG_KEY = "app.config";
  private static final String KEY = "__KEY__";
  private static final JsonObject configuration = new JsonObject();
  private static final Logger LOGGER = LoggerFactory.getLogger(AppConfiguration.class);

  public static AppConfiguration getInstance() {
    return Holder.INSTANCE;
  }

  private volatile boolean initialized = false;

  private AppConfiguration() {}

  @Override
  public void initializeComponent(Vertx vertx, JsonObject config) {
    if (!initialized) {
      synchronized (Holder.INSTANCE) {
        if (!initialized) {
          JsonObject appConfiguration = config.getJsonObject(APP_CONFIG_KEY);
          if (appConfiguration == null || appConfiguration.isEmpty()) {
            LOGGER.warn("App configuration is not available");
          } else {
            configuration.put(KEY, appConfiguration);
            initialized = true;
          }
        }
      }
    }
  }

  private static final class Holder {
    private static final AppConfiguration INSTANCE = new AppConfiguration();
  }

}
