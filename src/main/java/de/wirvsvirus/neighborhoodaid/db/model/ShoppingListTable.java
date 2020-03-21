package de.wirvsvirus.neighborhoodaid.db.model;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class ShoppingListTable {
    private final HashMap<UUID, ShoppingList> shoppingLists = new HashMap<>();

    public Optional<ShoppingList> getShoppingListById(final UUID id) {
        return Optional.ofNullable(shoppingLists.get(id));
    }

    public UUID addShoppingList(ShoppingList shoppingList) {
        final var uuid = UUID.randomUUID();
        final var prev = shoppingLists.put(uuid, shoppingList);
        return uuid;
    }

    /**
     *
     * @param uuid id for the shopping list to remove
     * @return true if removed, false otherwise
     */
    public boolean removeShoppingList(final UUID uuid) {
        return shoppingLists.remove(uuid) != null;
    }

}
