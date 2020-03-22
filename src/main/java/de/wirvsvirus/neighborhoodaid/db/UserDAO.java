package de.wirvsvirus.neighborhoodaid.db;

import de.wirvsvirus.neighborhoodaid.db.model.Address;
import de.wirvsvirus.neighborhoodaid.db.model.DataRoot;
import de.wirvsvirus.neighborhoodaid.db.model.User;
import de.wirvsvirus.neighborhoodaid.utils.BCrypt;

import java.util.UUID;

public class UserDAO {

    private final DbAccessor<DataRoot> accessor;

    public UserDAO(DbAccessor<DataRoot> accessor) {
        this.accessor = accessor;
    }

    public User createNewUser(User user) {
        //TODO: Bcyrpt
        final String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        final UUID randomUUID = UUID.randomUUID();
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

    public User getUserByMail(final String mail) {
        for (User user : accessor.getRoot().getUsers().values()) {
            if (user.getLogin().getData().equals(mail)) {
                return user;
            }
        }
        return null;
    }

}
