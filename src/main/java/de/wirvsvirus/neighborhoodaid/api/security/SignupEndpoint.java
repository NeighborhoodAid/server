package de.wirvsvirus.neighborhoodaid.api.security;

import de.wirvsvirus.neighborhoodaid.api.Endpoint;
import de.wirvsvirus.neighborhoodaid.api.utils.RestUtils;
import de.wirvsvirus.neighborhoodaid.db.UserDAO;
import de.wirvsvirus.neighborhoodaid.db.model.User;
import de.wirvsvirus.neighborhoodaid.utils.DbUtils;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import org.jetbrains.annotations.NotNull;

public class SignupEndpoint implements Endpoint {

    @Override
    public void setupRouting(@NotNull Vertx vertx, @NotNull Router router) {
        router.post().handler(ctx -> {
            DbUtils.getDbAccessor(vertx, consumer -> {
                RestUtils.getUserFromBodyOrFail(ctx).ifPresent(parsedUser -> {
                    if (parsedUser.getLogin().getType() != User.Login.LoginType.EMAIL) {
                        RestUtils.endResponseWithError(ctx, 400, "Please provide an email login.");
                    } else {
                        final var dao = new UserDAO(consumer);
                        final var existingUser = dao.getUserByMail(parsedUser.getLogin().getData());
                        if (existingUser == null) {
                            if(parsedUser.getPassword() == null || parsedUser.getPassword().isBlank()){
                                RestUtils.endResponseWithError(ctx, 400, "No password provided.");
                            }
                            else{
                                final var newUser = dao.createNewUser(parsedUser);
                                //TODO: Create jwt cookie
                                ctx.response().setStatusCode(201).end(Json.encodePrettily(newUser));
                            }
                        } else {
                            RestUtils.endResponseWithError(ctx, 400, "Mail already used.");
                        }
                    }
                });
            });
        });
    }
}
