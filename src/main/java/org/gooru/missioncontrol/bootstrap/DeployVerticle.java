package org.gooru.missioncontrol.bootstrap;

import java.util.ArrayList;
import java.util.List;
import org.gooru.missioncontrol.bootstrap.deployment.VerticleRegistry;
import org.gooru.missioncontrol.bootstrap.shutdown.Finalizer;
import org.gooru.missioncontrol.bootstrap.shutdown.Finalizers;
import org.gooru.missioncontrol.bootstrap.startup.Initializer;
import org.gooru.missioncontrol.bootstrap.startup.Initializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On 19-Jun-2019
 */
public class DeployVerticle extends AbstractVerticle {

  private final static Logger LOGGER = LoggerFactory.getLogger(DeployVerticle.class);

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    Future<Void> deployFuture = Future.future();
    Future<Void> startApplicationFuture = Future.future();

    deployVerticles(deployFuture);
    startApplication(startApplicationFuture);

    CompositeFuture.all(deployFuture, startApplicationFuture).setHandler(result -> {
      if (result.succeeded()) {
        LOGGER.info("all verticles deployed and application started successfully");
        startFuture.complete();
      } else {
        LOGGER.error("deployment or app startup failure", result.cause());
        startFuture.fail(result.cause());

        // Not much options now, no point in continue
        Runtime.getRuntime().halt(1);
      }
    });
  }

  private void deployVerticles(Future<Void> future) {
    VerticleRegistry registry = new VerticleRegistry();
    List<Future> futures = new ArrayList<>();
    for (String verticleName : registry) {
      Future<String> deployFuture = Future.future();
      futures.add(deployFuture);

      JsonObject config = config().getJsonObject(verticleName);
      if (config.isEmpty()) {
        vertx.deployVerticle(verticleName, deployFuture.completer());
      } else {
        DeploymentOptions options = new DeploymentOptions(config);
        vertx.deployVerticle(verticleName, options, deployFuture.completer());
      }
    }
    
    CompositeFuture.all(futures).setHandler(result -> {
      if (result.succeeded()) {
        LOGGER.info("all verticles deployed successfully");
        future.complete();
      } else {
        LOGGER.warn("verticle deployment failure", result.cause());
        future.fail(result.cause());
      }
    });
  }

  private void startApplication(Future<Void> startApplicationFuture) {
    vertx.executeBlocking(future -> {
      Initializers initializers = new Initializers();
      try {
        for (Initializer initializer : initializers) {
          initializer.initializeComponent(vertx, config());
        }
        future.complete();
      } catch (IllegalStateException ie) {
        LOGGER.error("error initializing application", ie);
        future.fail(ie);
      }
    }, result -> {
      if (result.succeeded()) {
        LOGGER.info("application initialized successfully");
        startApplicationFuture.complete();
      } else {
        LOGGER.warn("app startup failure", result.cause());
        startApplicationFuture.fail(result.cause());
      }
    });
  }

  @Override
  public void stop(Future<Void> stopFuture) throws Exception {
    vertx.executeBlocking(future -> {
      Finalizers finalizers = new Finalizers();
      for (Finalizer finalizer : finalizers) {
        finalizer.finalizeComponent();
      }
      future.complete();
    }, result -> {
      if (result.succeeded()) {
        LOGGER.info("component finalization for application shutdown done successfully");
        stopFuture.complete();
      } else {
        LOGGER.warn("app shutdown failure", result.cause());
        stopFuture.fail(result.cause());
      }
    });
  }

}
