package de.wirvsvirus.neighborhoodaid.db;

import de.wirvsvirus.neighborhoodaid.db.model.DataRoot;
import de.wirvsvirus.neighborhoodaid.db.model.ShoppingList;
import de.wirvsvirus.neighborhoodaid.db.model.User;

import java.util.UUID;

public class ShoppingListDAO {

    private final DbAccessor<DataRoot> accessor;

    public ShoppingListDAO(DbAccessor<DataRoot> accessor) {
        this.accessor = accessor;
    }

    public ShoppingList createShoppingList(User user, ShoppingList shoppingList) {
        final var uuid = UUID.randomUUID();
        final var newEntry = shoppingList.asNew(uuid, user.getId());
        final var table = accessor.getRoot().getShoppingLists();
        final var prev = table.put(newEntry.getId(), newEntry);
        if (prev != null) {
            table.put(prev.getId(), prev);
            throw new IllegalStateException("Tried to create existing list with id '" + newEntry.getId() + "'.");
        }
        final var loadUser = accessor.getRoot().getUsers().get(user.getId());
        loadUser.getShoppingLists().add(uuid);
        accessor.storeAll(table, loadUser.getShoppingLists());
        return newEntry;
    }

    public ShoppingList updateShoppingList(User user, UUID uuid, ShoppingList shoppingList) {
        final var shoppingTable = accessor.getRoot().getShoppingLists();
        final var current = shoppingTable.get(uuid);
        if (isShoppingListOwner(accessor.getRoot().getUsers().get(user.getId()), current)) {
            final var newEntry = current.withNewVariables(shoppingList);
            shoppingTable.put(newEntry.getId(), newEntry);
            accessor.store(shoppingTable);
            return newEntry;
        } else {
            throw new IllegalStateException("The user is not the owner of the shoppingList");
        }
    }

    public ShoppingList deleteShoppingList(User user, ShoppingList shoppingList) {
        final var loadUser = accessor.getRoot().getUsers().get(user.getId());
        if (isShoppingListOwner(loadUser, shoppingList)) {
            loadUser.getShoppingLists().remove(shoppingList.getId());
            final var deleted = accessor.getRoot().getShoppingLists().remove(shoppingList.getId());
            accessor.storeAll(loadUser.getShoppingLists(), accessor.getRoot().getShoppingLists());
            return deleted;
        } else {
            throw new IllegalStateException("The user is not the owner of the shoppingList");
        }
    }

    public ShoppingList claimShoppingList(User user, ShoppingList shoppingList) {
        final var uuid = user.getId();
        final var newEntry = shoppingList.withClaimer(uuid);
        final var table = accessor.getRoot().getShoppingLists();
        final var prev = table.put(newEntry.getId(), newEntry);
        if (prev == null) {
            table.remove(newEntry.getId());
            throw new IllegalStateException("Tried to update not existing list with id '" + newEntry.getId() + "'.");
        }
        accessor.store(table);
        return newEntry;
    }

    private boolean isShoppingListOwner(User user, ShoppingList shoppingList) {
        System.out.println(shoppingList.getId() + " : " + user.getShoppingLists().toString());
        return user.getShoppingLists().contains(shoppingList.getId());
    }
}
