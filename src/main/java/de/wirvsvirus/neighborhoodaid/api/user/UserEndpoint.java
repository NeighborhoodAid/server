package de.wirvsvirus.neighborhoodaid.api.user;

import de.wirvsvirus.neighborhoodaid.api.Endpoint;
import de.wirvsvirus.neighborhoodaid.api.utils.RestUtils;
import de.wirvsvirus.neighborhoodaid.db.UserDAO;
import de.wirvsvirus.neighborhoodaid.geolocation.GeoLocation;
import de.wirvsvirus.neighborhoodaid.utils.DbUtils;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UserEndpoint implements Endpoint {
    @Override
    public void setupRouting(@NotNull Vertx vertx, @NotNull Router router) {
        router.get("/:id").handler(ctx -> getUser(vertx, ctx));
        router.post("/:id").handler(ctx -> updateUser(vertx, ctx));
    }

    private void getUser(Vertx vertx, RoutingContext ctx) {
        final var id = ctx.request().getParam("id");
        try {
            final var uuid = UUID.fromString(id);
            DbUtils.getDbAccessor(vertx, accessor -> {
                final var user = accessor.getRoot().getUsers().get(uuid);
                if (user == null) {
                    RestUtils.endResponseWithError(ctx, 404, "User with id '" + uuid + "' not found.");
                } else {
                    ctx.response().end(Json.encodePrettily(user));
                }
            });
        } catch (IllegalArgumentException ex) {
            RestUtils.endResponseWithError(ctx, 400, ex.getLocalizedMessage());
        }
    }

    private void updateUser(Vertx vertx, RoutingContext ctx) {
        final var id = ctx.request().getParam("id");
        try {
            final var uuid = UUID.fromString(id);
            DbUtils.getDbAccessor(vertx, accessor -> {
                final var user = accessor.getRoot().getUsers().get(uuid);
                if (user == null) {
                    RestUtils.endResponseWithError(ctx, 404, "User with id '" + uuid + "' not found.");
                } else {
                    RestUtils.getUserFromBodyOrFail(ctx).ifPresent(body -> {
                        final var address = body.getAddress();
                        if (address == null) {
                            RestUtils.endResponseWithError(ctx, 500, "Missing address field!");
                        } else {
                            GeoLocation.requestGeoLocation(vertx, address, res -> {
                                final var dao = new UserDAO(accessor);
                                if (res.succeeded()) {
                                    final var newUser = dao.updateUser(uuid, body.withNewAddress(res.result()));
                                    ctx.response().end(Json.encodePrettily(newUser));
                                } else {
                                    RestUtils.endResponseWithError(ctx, 500, "Could not request address: " + res.cause().getLocalizedMessage());
                                }
                            });
                        }
                    });
                }
            });
        } catch (IllegalArgumentException ex) {
            RestUtils.endResponseWithError(ctx, 400, ex.getLocalizedMessage());
        }
    }
}
