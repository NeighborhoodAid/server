package de.wirvsvirus.neighborhoodaid;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertxLauncher extends Launcher {

    public static void main(String[] args) {
        new VertxLauncher().dispatch(args);
    }

    private static final Logger logger = LoggerFactory.getLogger(VertxLauncher.class);

    @Override
    public void afterStartingVertx(Vertx vertx) {
        super.afterStartingVertx(vertx);
        vertx.exceptionHandler(exc -> logger.error("", exc));
    }

    @Override
    public void handleDeployFailed(Vertx vertx, String mainVerticle, DeploymentOptions deploymentOptions, Throwable cause) {
        super.handleDeployFailed(vertx, mainVerticle, deploymentOptions, cause);
        logger.error(String.format("Failed to deploy verticle %s.", mainVerticle), cause);
    }

    @Override
    protected String getMainVerticle() {
        return StarterVerticle.class.getCanonicalName();

    }

}
