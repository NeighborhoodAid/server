package de.wirvsvirus.neighborhoodaid.api.list;

import de.wirvsvirus.neighborhoodaid.api.Endpoint;
import de.wirvsvirus.neighborhoodaid.api.MediaTypes;
import de.wirvsvirus.neighborhoodaid.api.utils.RestUtils;
import de.wirvsvirus.neighborhoodaid.db.model.ShoppingList;
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

    }

    private void handleGet(Vertx vertx, RoutingContext ctx) {
        final var id = ctx.request().getParam("id");
        try {
            final var uuid = UUID.fromString(id);
            DbUtils.getDbAccessor(vertx, accessor -> {
                final var list = accessor.getRoot().getShoppingListTable().getShoppingListById(uuid);
                list.ifPresentOrElse(obj -> {
                    ctx.response().setStatusCode(200).end(Json.encodePrettily(list));
                }, () -> {
                    RestUtils.endResponseWithError(ctx, 404, "No shopping list with id '" + uuid + "' found.");
                });
            });
        } catch (IllegalArgumentException ex) {
            RestUtils.endResponseWithError(ctx, 400, ex.getLocalizedMessage());
        }
    }

    private void handleCreate(Vertx vertx, RoutingContext ctx) {
        final var list = ctx.getBodyAsJson().mapTo(ShoppingList.class);
        DbUtils.getDbAccessor(vertx, accessor -> {
            accessor.getRoot().getUserTable();
            final var table = accessor.getRoot().getShoppingListTable();
            final var newList = table.addShoppingList(list);
            //TODO complete
        });
    }
}
