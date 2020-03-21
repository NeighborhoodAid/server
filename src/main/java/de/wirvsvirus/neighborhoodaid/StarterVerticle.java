package de.wirvsvirus.neighborhoodaid;

import de.wirvsvirus.neighborhoodaid.api.RestVerticle;
import de.wirvsvirus.neighborhoodaid.db.DbVerticle;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            vertx.deployVerticle(DbVerticle.class.getCanonicalName(), res -> {
                if (res.succeeded()) {
                    vertx.deployVerticle(RestVerticle.class.getCanonicalName(),
                            new DeploymentOptions().setConfig(config),
                            rest -> {
                                if (rest.succeeded()) {
                                    logger.info("Application successful deployed.");
                                } else {
                                    startPromise.fail(rest.cause());
                                }
                            });
                } else {
                    startPromise.fail(res.cause());
                }
            });
        });
    }
}
