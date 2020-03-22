package de.wirvsvirus.neighborhoodaid.db;

import de.wirvsvirus.neighborhoodaid.db.model.Address;
import de.wirvsvirus.neighborhoodaid.db.model.DataRoot;
import de.wirvsvirus.neighborhoodaid.db.model.User;

import java.util.UUID;

public class UserDAO {

    private final DbAccessor<DataRoot> accessor;

    public UserDAO(DbAccessor<DataRoot> accessor) {
        this.accessor = accessor;
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

}
