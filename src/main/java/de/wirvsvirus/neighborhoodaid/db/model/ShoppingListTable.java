package de.wirvsvirus.neighborhoodaid.db.model;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class ShoppingListTable {
    private final HashMap<UUID, ShoppingList> shoppingLists = new HashMap<>();

    public Optional<ShoppingList> getShoppingListById(final UUID id) {
        return Optional.ofNullable(shoppingLists.get(id));
    }

    public ShoppingList getShoppingList(final UUID uuid) {
        return shoppingLists.get(uuid);
    }

    public ShoppingList putShoppingList(final ShoppingList shoppingList) {
        return shoppingLists.put(shoppingList.getId(), shoppingList);
    }

    public ShoppingList deleteShoppingList(final ShoppingList shoppingList) {
        return shoppingLists.remove(shoppingList.getId());
    }
}
