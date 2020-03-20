package de.wirvsvirus.neighborhoodaid;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;

public class RestVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(RestVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        logger.info("Starting server");

        Router router = Router.router(vertx);
        router.get("/").handler(ctx -> ctx.response().end("<h1>Start page</h1>"));
        router.get("/api/v1/health").handler(ctx -> {
            final var uptime = ManagementFactory.getRuntimeMXBean().getUptime();
            final var formatted = DurationFormatUtils.formatDurationHMS(uptime);
            ctx.response().end("<h1 style=\"color:green\")>Service is running since " + formatted + "</h1>");
        });

        HttpServer server = vertx.createHttpServer();
        server.requestHandler(router).listen(8080, res -> {
            if (res.succeeded()) {
                logger.info("Webserver running.");
            } else {
                logger.error("Error while starting the webserver.", res.cause());
            }
        });
    }
}
