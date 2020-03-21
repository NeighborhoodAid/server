package de.wirvsvirus.neighborhoodaid.api.list;

import de.wirvsvirus.neighborhoodaid.api.Endpoint;
import de.wirvsvirus.neighborhoodaid.api.MediaTypes;
import de.wirvsvirus.neighborhoodaid.api.utils.RestUtils;
import de.wirvsvirus.neighborhoodaid.db.DbAccessor;
import de.wirvsvirus.neighborhoodaid.db.model.DbRoot;
import de.wirvsvirus.neighborhoodaid.db.model.ShoppingList;
import de.wirvsvirus.neighborhoodaid.db.model.User;
import de.wirvsvirus.neighborhoodaid.utils.DbUtils;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ListEndpoint implements Endpoint {

    @Override
    public void setupRouting(@NotNull Vertx vertx, @NotNull Router router) {
        router.get("/:id").produces(MediaTypes.JSON).handler(ctx -> handleGet(vertx, ctx));
        router.post().handler(ctx -> handleCreate(vertx, ctx));
        router.put("/:id").handler(ctx -> handleUpdate(vertx, ctx));
        router.post("/:id/claim").handler(ctx -> handleClaim(vertx, ctx));
        router.delete("/:id").handler(ctx -> handleDelete(vertx, ctx));
    }

    private void handleGet(Vertx vertx, RoutingContext ctx) {
        DbUtils.getDbAccessor(vertx, accessor -> {
            final var user = handleAuthentication(ctx, accessor);
            if (user != null) {
                final var list = validateListId(ctx, accessor);
                if (list != null) {
                    ctx.response().setStatusCode(200).end(Json.encodePrettily(list));
                }
            }
        });
    }

    private void handleCreate(Vertx vertx, RoutingContext ctx) {
        DbUtils.getDbAccessor(vertx, accessor -> {
            final var user = handleAuthentication(ctx, accessor);
            if (user != null) {
                final var list = ctx.getBodyAsJson().mapTo(ShoppingList.class);
                final var table = accessor.getRoot().getShoppingListTable();
                final var listWithId = table.addShoppingList(list, user.getId());
                accessor.store();
                ctx.response().setStatusCode(201).end(Json.encodePrettily(listWithId));
            }
        });
    }

    private void handleUpdate(Vertx vertx, RoutingContext ctx) {
        DbUtils.getDbAccessor(vertx, accessor -> {
            final var user = handleAuthentication(ctx, accessor);
            if (user != null) {
                final var list = validateListId(ctx, accessor);
                if (list != null) {
                    final var newList = accessor.getRoot().getShoppingListTable().updateShoppingList(list);
                    accessor.store();
                    ctx.response().setStatusCode(200).end(Json.encodePrettily(newList));
                }
            }
        });
    }

    private void handleDelete(Vertx vertx, RoutingContext ctx) {
        DbUtils.getDbAccessor(vertx, accessor -> {
            final var user = handleAuthentication(ctx, accessor);
            if (user != null) {
                final var list = validateListId(ctx, accessor);
                if (list != null) {
                    //TODO handle User shopping lists
                    accessor.getRoot().getShoppingListTable().removeShoppingList(list.getId());
                    accessor.store();
                    ctx.response().setStatusCode(200).end(Json.encodePrettily(list));
                }
            }
        });
    }

    private void handleClaim(Vertx vertx, RoutingContext ctx) {
        DbUtils.getDbAccessor(vertx, accessor -> {
            final var user = handleAuthentication(ctx, accessor);
            if (user != null) {
                final var list = validateListId(ctx, accessor);
                if (list != null) {
                    final var newList = accessor.getRoot().getShoppingListTable().claimShoppingList(list, user.getId());
                    accessor.store();
                    ctx.response().setStatusCode(200).end(Json.encodePrettily(newList));
                }
            }
        });
    }

    private User handleAuthentication(RoutingContext ctx, DbAccessor<DbRoot> accessor) {
        final var auth = RestUtils.getAuthorization(ctx);
        if (auth == null) {
            RestUtils.endResponseWithMissingAuthorization(ctx);
        } else {
            try {
                final var uuid = UUID.fromString(auth);
                final var userTable = accessor.getRoot().getUserTable();
                final var opt = userTable.getUserById(uuid);
                if (opt.isPresent()) {
                    return opt.get();
                } else {
                    RestUtils.endResponseWithInvalidAuthorization(ctx);
                }
            } catch (IllegalArgumentException ex) {
                RestUtils.endResponseWithInvalidAuthorization(ctx, ex.getLocalizedMessage());
            }
        }
        return null;
    }

    private ShoppingList validateListId(RoutingContext ctx, DbAccessor<DbRoot> accessor) {
        try {
            final var id = ctx.request().getParam("id");
            final var uuid = UUID.fromString(id);
            final var opt = accessor.getRoot().getShoppingListTable().getShoppingListById(uuid);
            if (opt.isPresent()) {
                return opt.get();
            } else {
                RestUtils.endResponseWithError(ctx, 404, "No shopping list with id '" + uuid + "' found.");
            }
        } catch (IllegalArgumentException ex) {
            RestUtils.endResponseWithError(ctx, 400, ex.getLocalizedMessage());
        }
        return null;
    }
}
