package de.wirvsvirus.neighborhoodaid.api.security;

import de.wirvsvirus.neighborhoodaid.api.Endpoint;
import de.wirvsvirus.neighborhoodaid.api.utils.RestUtils;
import de.wirvsvirus.neighborhoodaid.db.UserDAO;
import de.wirvsvirus.neighborhoodaid.db.model.User;
import de.wirvsvirus.neighborhoodaid.utils.BCrypt;
import de.wirvsvirus.neighborhoodaid.utils.DbUtils;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import org.jetbrains.annotations.NotNull;

public class LoginEndpoint implements Endpoint {

    @Override
    public void setupRouting(@NotNull Vertx vertx, @NotNull Router router) {
        router.post().handler(ctx -> {
            RestUtils.getUserFromBodyOrFail(ctx).ifPresent(user -> {
                final var login = user.getLogin();
                if (login == null || login.getType() != User.Login.LoginType.EMAIL) {
                    RestUtils.endResponseWithError(ctx, 400, "Please provide email login.");
                } else if (user.getPassword() == null || user.getPassword().isBlank()) {
                    RestUtils.endResponseWithError(ctx, 400, "No password provided.");
                } else {
                    DbUtils.getDbAccessor(vertx, accessor -> {
                        final var dao = new UserDAO(accessor);
                        final var mailUser = dao.getUserByMail(login.getData());
                        if (mailUser == null) {
                            RestUtils.endResponseWithError(ctx, 404, "Invalid email.");
                        } else {
                            final String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
                            if (mailUser.getPassword().equals(hashedPassword)) {
                                ctx.response().end(Json.encodePrettily(mailUser));
                            } else {
                                RestUtils.endResponseWithError(ctx, 400, "Invalid password.");
                            }
                        }
                    });
                }
            });
        });
    }
}
