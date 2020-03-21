package de.wirvsvirus.neighborhoodaid.db.model;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class UserTable {
    private final HashMap<UUID, User> users = new HashMap<>();

    public User addUser(final User user) {
        //TODO generated
        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> getUserById(final UUID uuid) {
        return Optional.ofNullable(users.get(uuid));
    }

}
