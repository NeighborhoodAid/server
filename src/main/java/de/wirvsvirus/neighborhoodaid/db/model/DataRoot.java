package de.wirvsvirus.neighborhoodaid.db.model;

import java.util.HashMap;
import java.util.UUID;

public class DataRoot {

    private final HashMap<UUID, User> users = new HashMap<>();
    private final HashMap<UUID, ShoppingList> shoppingLists = new HashMap<>();

    public DataRoot() {
        super();
    }

    public HashMap<UUID, User> getUsers() {
        return users;
    }

    public HashMap<UUID, ShoppingList> getShoppingLists() {
        return shoppingLists;
    }
}
