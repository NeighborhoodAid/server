package de.wirvsvirus.neighborhoodaid.api;

import de.wirvsvirus.neighborhoodaid.oauth.GAuth;
import de.wirvsvirus.neighborhoodaid.oauth.GCallback;
import de.wirvsvirus.neighborhoodaid.api.list.ListEndpoint;
import de.wirvsvirus.neighborhoodaid.api.security.LoginEndpoint;
import de.wirvsvirus.neighborhoodaid.api.security.SignupEndpoint;
import de.wirvsvirus.neighborhoodaid.api.stats.HealthEndpoint;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(RestVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) {
        logger.info("Starting server");

        Router router = Router.router(vertx);
        //Required for POST body and file upload handling
        router.route("/api/v1/*").handler(BodyHandler.create());
        router.get("/").handler(ctx -> ctx.response().end("<h1>Start page</h1>"));

        registerEndpoint("/api/v1/health", router, new HealthEndpoint());
        registerEndpoint("/api/v1/signup", router, new SignupEndpoint());
        registerEndpoint("/api/v1/login", router, new LoginEndpoint());
        registerEndpoint("/api/v1/list", router, new ListEndpoint());
        registerEndpoint("/gauth", router, new GAuth());
        registerEndpoint("/gcallback", router, new GCallback());

        HttpServer server = vertx.createHttpServer();
        server.requestHandler(router).listen(8080, res -> {
            if (res.succeeded()) {
                logger.info("Webserver running.");
                startPromise.complete();
            } else {
                logger.error("Error while starting the webserver.", res.cause());
                startPromise.fail(res.cause());
            }
        });
    }

    private void registerEndpoint(@NotNull final String mountPoint, @NotNull final Router router, @NotNull final Endpoint endpoint) {
        final var subRouter = Router.router(vertx);
        endpoint.setupRouting(vertx, subRouter);
        router.mountSubRouter(mountPoint, subRouter);
        logger.info("Registered endpoint '" + mountPoint + "'.");
    }
}
