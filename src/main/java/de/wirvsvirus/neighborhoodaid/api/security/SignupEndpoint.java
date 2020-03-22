package de.wirvsvirus.neighborhoodaid.api.security;

import de.wirvsvirus.neighborhoodaid.api.Endpoint;
import de.wirvsvirus.neighborhoodaid.api.utils.RestUtils;
import de.wirvsvirus.neighborhoodaid.db.model.DataRoot;
import de.wirvsvirus.neighborhoodaid.db.model.User;
import de.wirvsvirus.neighborhoodaid.utils.BCrypt;
import de.wirvsvirus.neighborhoodaid.utils.DbUtils;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;

public class SignupEndpoint implements Endpoint {

    @Override
    public void setupRouting(@NotNull Vertx vertx, @NotNull Router router) {
        router.get().handler(ctx -> {
            DbUtils.getDbAccessor(vertx, consumer -> {
                final User parsedUser = ctx.getBodyAsJson().mapTo(User.class);
                if(parsedUser.getLogin().getType() != User.Login.LoginType.EMAIL){
                    RestUtils.endResponseWithError(ctx, 400, "Please provide an email login");
                }
                if(getUserByMail(parsedUser.getLogin().getData(), consumer.getRoot()) == null){
                    //TODO: Bcyrpt
                    final String hashedPassword = BCrypt.hashpw(parsedUser.getPassword(), BCrypt.gensalt());
                    final UUID randomUUID = UUID.randomUUID();
                    final User user = new User(randomUUID, parsedUser.getName(), parsedUser.getLogin(), hashedPassword, parsedUser.getPhoneNumber(), parsedUser.getAddress(), parsedUser.getShoppingLists());
                    
                    consumer.getRoot().getUsers().put(randomUUID, user);

                    //TODO: Create jwt cookie
                    ctx.request().response().setStatusCode(201).end(Json.encodePrettily(user));
                } else {
                    
                    //TODO: Error response
                }
            });
        });
    }

    public User getUserByMail(final String mail, final DataRoot dataRoot){
        for(User user : dataRoot.getUsers().values()){
            if(user.getLogin().getData().equals(mail)){
                return user;
            }
        }
        return null;
    }
}
