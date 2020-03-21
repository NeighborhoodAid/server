package de.wirvsvirus.neighborhoodaid.db.model;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class ShoppingListTable {
    private final HashMap<UUID, ShoppingList> shoppingLists = new HashMap<>();

    public Optional<ShoppingList> getShoppingListById(final UUID id) {
        return Optional.ofNullable(shoppingLists.get(id));
    }

    public ShoppingList addShoppingList(ShoppingList shoppingList, UUID creator) {
        final var uuid = UUID.randomUUID();
        final var newList = shoppingList.withId(uuid).withCreator(creator);
        final var prev = shoppingLists.put(uuid, newList);
        if (prev != null) {
            shoppingLists.put(uuid, prev);
            throw new IllegalStateException("Tried to add existing list with id '" + newList + "'.");
        }
        return newList;
    }

    public ShoppingList updateShoppingList(ShoppingList shoppingList) {
        //TODO Claimer manipulation possible
        final var uuid = shoppingList.getId();
        final var prev = shoppingLists.put(uuid, shoppingList);
        if (prev == null) {
            shoppingLists.remove(uuid);
            throw new IllegalStateException("Tried to update not existing list with id '" + uuid + "'.");
        }
        return shoppingList;
    }

    public ShoppingList claimShoppingList(ShoppingList shoppingList, UUID claimer) {
        final var newList = shoppingList.withClaimer(claimer);
        final var prev = shoppingLists.put(newList.getId(), newList);
        if (prev == null) {
            shoppingLists.remove(newList.getId());
            throw new IllegalStateException("Tried to update not existing list with id '" + newList.getId() + "'.");
        }
        return newList;
    }

    public ShoppingList removeShoppingList(final UUID uuid) {
        return shoppingLists.remove(uuid);
    }

}
