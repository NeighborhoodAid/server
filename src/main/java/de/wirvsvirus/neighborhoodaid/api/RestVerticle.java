package de.wirvsvirus.neighborhoodaid.api;

import de.wirvsvirus.neighborhoodaid.api.list.ListEndpoint;
import de.wirvsvirus.neighborhoodaid.api.oauth.GAuth;
import de.wirvsvirus.neighborhoodaid.api.security.LoginEndpoint;
import de.wirvsvirus.neighborhoodaid.api.security.SignupEndpoint;
import de.wirvsvirus.neighborhoodaid.api.stats.HealthEndpoint;
import de.wirvsvirus.neighborhoodaid.api.user.UserEndpoint;
import de.wirvsvirus.neighborhoodaid.db.model.Address;
import de.wirvsvirus.neighborhoodaid.db.model.User;
import de.wirvsvirus.neighborhoodaid.utils.ConfigUtils;
import de.wirvsvirus.neighborhoodaid.utils.DbUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.UUID;

public class RestVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(RestVerticle.class);
    private final static UUID TEST_USER_UUID = UUID.fromString("550e8400-e29b-11d4-a716-446655440000");


    @Override
    public void start(Promise<Void> startPromise) {
        logger.info("Starting server");

        checkTestUser();

        final JWTAuth jwt = JWTAuth.create(vertx, new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions(config().getJsonObject("jwt"))
                        .setAlgorithm("HS256")
                        .setSymmetric(true)));

        final Router router = Router.router(vertx);
        if (ConfigUtils.isDevModeActive(config())) {
            logger.warn("DEV MODE: JWT is disabled.");
        } else {
            router.route("/api/*").handler(JWTAuthHandler.create(jwt, "/api/v1/session"));
        }
        //Required for POST body and file upload handling
        router.route("/api/*").handler(BodyHandler.create());
        router.get("/").handler(ctx -> ctx.response().end("<h1>Start page</h1>"));

        registerEndpoint("/api/v1/health", router, new HealthEndpoint());
        registerEndpoint("/api/v1/auth/signup", router, new SignupEndpoint());
        registerEndpoint("/api/v1/auth/login", router, new LoginEndpoint());
        registerEndpoint("/api/v1/list", router, new ListEndpoint());
        registerEndpoint("/api/v1/user", router, new UserEndpoint());
        registerEndpoint("/oauth", router, new GAuth(config(), jwt));

        final HttpServer server = vertx.createHttpServer();
        final var port = ConfigUtils.getHttpPort(config());
        server.requestHandler(router).listen(port, res -> {
            if (res.succeeded()) {
                logger.info("Webserver running on port {}.", port);
                startPromise.complete();
            } else {
                logger.error("Error while starting the webserver on port {}.", port, res.cause());
                startPromise.fail(res.cause());
            }
        });
    }

    private void registerEndpoint(@NotNull final String mountPoint, @NotNull final Router router,
                                  @NotNull final Endpoint endpoint) {
        final var subRouter = Router.router(vertx);
        endpoint.setupRouting(vertx, subRouter);
        router.mountSubRouter(mountPoint, subRouter);
        logger.info("Registered endpoint '" + mountPoint + "'.");
    }

    private void checkTestUser() {
        DbUtils.getDbAccessor(vertx, accessor -> {
            final var users = accessor.getRoot().getUsers();
            if (ConfigUtils.isDevModeActive(config())) {
                users.computeIfAbsent(TEST_USER_UUID, (uuid) -> new User(uuid, User.Login.email("test@test.org"), "Tester",
                        "test@test.org", "unhashed", "+49123456789",
                        Address.empty(), new ArrayList<>()));
                accessor.store(users);
                logger.warn("DEV MODE: Created test user with id '{}'.", TEST_USER_UUID);
            } else if (users.containsKey(TEST_USER_UUID)) {
                users.remove(TEST_USER_UUID);
                accessor.store(users);
                logger.info("Removed test user from previous dev mode with the id '{}'.", TEST_USER_UUID);
            }
        });
    }
}
