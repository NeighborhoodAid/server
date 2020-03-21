package de.wirvsvirus.neighborhoodaid;

import de.wirvsvirus.neighborhoodaid.api.RestVerticle;
import de.wirvsvirus.neighborhoodaid.db.DbVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StarterVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(StarterVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) {
        logger.info("Starting application modules...");
        vertx.deployVerticle(DbVerticle.class.getCanonicalName(), res -> {
            if(res.succeeded()){
                vertx.deployVerticle(RestVerticle.class.getCanonicalName(), rest -> {
                    if (rest.succeeded()) {
                        logger.info("Application successful deployed.");
                    }
                    else{
                        logger.error("Error starting rest-api verticle.", res.cause());
                        Runtime.getRuntime().exit(15);
                    }
                });
            }
            else{
                logger.error("Error starting database verticle.", res.cause());
                Runtime.getRuntime().exit(15);
            }
        });
    }
}
