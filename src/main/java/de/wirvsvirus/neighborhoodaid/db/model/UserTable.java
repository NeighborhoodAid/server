package de.wirvsvirus.neighborhoodaid.db.model;

import java.util.HashMap;
import java.util.UUID;

public class UserTable {
    private final HashMap<UUID, User> users = new HashMap<>();

    public User addUser(final User user) {
        final var uuid = UUID.randomUUID();

        users.put(uuid, user);
        return user;
    }

}
