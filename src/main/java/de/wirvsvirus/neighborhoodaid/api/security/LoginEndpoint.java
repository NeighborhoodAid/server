package de.wirvsvirus.neighborhoodaid.api.security;

import de.wirvsvirus.neighborhoodaid.api.Endpoint;
import de.wirvsvirus.neighborhoodaid.utils.StandardResponses;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.jetbrains.annotations.NotNull;

public class LoginEndpoint implements Endpoint {

    @Override
    public void setupRouting(@NotNull Vertx vertx, @NotNull Router router) {
        router.get().handler(ctx -> ctx.response().end(StandardResponses.ENDPOINT_NOT_IMPLEMENTED));
    }
}
