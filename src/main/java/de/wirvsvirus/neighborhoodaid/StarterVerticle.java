package de.wirvsvirus.neighborhoodaid;

import de.wirvsvirus.neighborhoodaid.api.RestVerticle;
import de.wirvsvirus.neighborhoodaid.db.DbVerticle;
import de.wirvsvirus.neighborhoodaid.utils.ConfigUtils;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StarterVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(StarterVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) {
        logger.info("Starting application modules...");
        ConfigStoreOptions workDirStore = new ConfigStoreOptions()
                .setType("file")
                .setConfig(new JsonObject()
                        .put("path", "config/config.json"));
        ConfigRetrieverOptions options = new ConfigRetrieverOptions()
                .addStore(workDirStore);
        ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
        retriever.getConfig(json -> {
            if (json.failed()) {
                startPromise.fail(json.cause());
                return;
            }
            JsonObject config = json.result();
            checkDevMode(config);
            deployInSequence(config,
                    startPromise,
                    DbVerticle.class,
                    RestVerticle.class);
        });
    }

    private void checkDevMode(final JsonObject json) {
        if (ConfigUtils.isDevModeActive(json)) {
            logger.warn("------ DEV MODE: ACTIVATED -------");
            logger.warn("Some bypass functionality is active which definitely breaks security! DO NOT USE IN PRODUCTION!");
            logger.warn("-----------------------------------");
        }
    }

    private void deployInSequence(JsonObject config, Promise<Void> startPromise, Class<?>... toStart) {
        deployInSequence(new DeploymentOptions().setConfig(config), startPromise,
                new ArrayList<>(Arrays.asList(toStart)));
    }

    private void deployInSequence(DeploymentOptions options, Promise<Void> startPromise, List<Class<?>> toDeploy) {
        if (toDeploy.isEmpty()) {
            logger.info("Application startup complete");
            startPromise.complete();
            return;
        }
        @SuppressWarnings("unchecked")
        Class<? extends Verticle> deployNow = (Class<? extends Verticle>) toDeploy.remove(0);
        logger.info("Now deploying verticle {}", deployNow.getCanonicalName());
        vertx.deployVerticle(deployNow, options, res -> {
            if (res.failed()) {
                startPromise.fail(res.cause());
            } else {
                logger.info("Successfully deployed verticle {}", deployNow.getCanonicalName());
                deployInSequence(options, startPromise, toDeploy);
            }
        });
    }
}
