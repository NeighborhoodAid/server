package de.wirvsvirus.neighborhoodaid.db;

import de.wirvsvirus.neighborhoodaid.db.model.Address;
import de.wirvsvirus.neighborhoodaid.db.model.DataRoot;
import de.wirvsvirus.neighborhoodaid.db.model.User;
import de.wirvsvirus.neighborhoodaid.utils.BCrypt;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class UserDAO {

    private final DbAccessor<DataRoot> accessor;

    public UserDAO(DbAccessor<DataRoot> accessor) {
        this.accessor = accessor;
    }

    public User createNewUser(User user) {
        final UUID randomUUID = UUID.randomUUID();
        final String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        final User newEntry = user.withNewIdAndPassword(randomUUID, hashedPassword);
        final var users = accessor.getRoot().getUsers();
        final var prev = users.put(randomUUID, newEntry);
        if (prev != null) {
            users.put(randomUUID, prev);
            throw new IllegalStateException("User with uuid '" + randomUUID + "' already exists.");
        }
        accessor.store(users);
        return newEntry;
    }

    public User createNewGAuthUser(User.Login login, JsonObject json) {
        final UUID randomUUID = UUID.randomUUID();
        final var users = accessor.getRoot().getUsers();
        final User newEntry = new User(randomUUID, login, json.getString("given_name", "Missing") + json.getString("family_name", ""),
                json.getString("email", ""), null, "", Address.empty(), new ArrayList<>());
        final var prev = users.put(randomUUID, newEntry);
        if (prev != null) {
            users.put(randomUUID, prev);
            throw new IllegalStateException("User with uuid '" + randomUUID + "' already exists.");
        }
        accessor.store(users);
        return newEntry;
    }

    public User updateUser(UUID uuid, User update) {
        final var users = accessor.getRoot().getUsers();
        final var user = users.get(uuid);
        final var newEntry = user.withUpdate(update);
        final var prev = users.put(uuid, newEntry);
        if (prev == null) {
            users.remove(uuid);
            throw new IllegalStateException("Tried to update not existing user with id '" + uuid + "'.");
        }
        accessor.store(users);
        return newEntry;
    }

    public Optional<User> getUserByLogin(User.Login login) {
        return accessor.getRoot().getUsers().values().stream()
                .filter(user->user.getLogin().equals(login)).findFirst();
    }
    public User getUserByRoutingContext(RoutingContext ctx){
        return getUserByLogin(User.Login.fromString(ctx.user().principal().getString("login"))).orElseThrow();
    }
    public User getUserByMail(final String mail) {
        for (User user : accessor.getRoot().getUsers().values()) {
            if (user.getLogin().getId().equals(mail)) {
                return user;
            }
        }
        return null;
    }

}
