package de.wirvsvirus.neighborhoodaid.db;

import de.wirvsvirus.neighborhoodaid.db.model.DbRoot;
import de.wirvsvirus.neighborhoodaid.db.model.ShoppingList;
import de.wirvsvirus.neighborhoodaid.db.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ShoppingListDAO {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingListDAO.class);

    private final DbAccessor<DbRoot> accessor;

    public ShoppingListDAO(DbAccessor<DbRoot> accessor) {
        this.accessor = accessor;
    }

    public ShoppingList createShoppingList(User user, ShoppingList shoppingList) {
        final var uuid = UUID.randomUUID();
        final var newEntry = shoppingList.asNew(uuid, user.getId());
        final var table = accessor.getRoot().getShoppingListTable();
        final var prev = table.putShoppingList(newEntry);
        if (prev != null) {
            table.putShoppingList(prev);
            throw new IllegalStateException("Tried to create existing list with id '" + newEntry.getId() + "'.");
        }
        final var loadUser = accessor.getRoot().getUserTable().getUserById(user.getId());
        loadUser.get().getShoppingLists().add(uuid);
        accessor.store();
        return newEntry;
    }

    public ShoppingList updateShoppingList(User user, UUID uuid, ShoppingList shoppingList) {
        final var shoppingTable = accessor.getRoot().getShoppingListTable();
        final var current = shoppingTable.getShoppingList(uuid);
            logger.debug("NewValues: " + shoppingList);
            logger.debug("Current: " + current);
        if (isShoppingListOwner(user, current)) {
            final var newEntry = current.withNewVariables(shoppingList);
            logger.debug("Merged: " + newEntry);
            shoppingTable.putShoppingList(newEntry);
            accessor.store();
            return newEntry;
        } else {
            throw new IllegalStateException("The user is not the owner of the shoppingList");
        }
    }

    public ShoppingList deleteShoppingList(User user, ShoppingList shoppingList) {
        final var loadUser = accessor.getRoot().getUserTable().getUserById(user.getId()).get();
        if (isShoppingListOwner(loadUser, shoppingList)) {
            loadUser.getShoppingLists().remove(shoppingList.getId());
            final var deleted = deleteShoppingList(loadUser, shoppingList);
            accessor.store();
            return deleted;
        } else {
            throw new IllegalStateException("The user is not the owner of the shoppingList");
        }
    }

    public ShoppingList claimShoppingList(User user, ShoppingList shoppingList) {
        final var uuid = user.getId();
        final var newEntry = shoppingList.withClaimer(uuid);
        final var table = accessor.getRoot().getShoppingListTable();
        final var prev = table.putShoppingList(newEntry);
        if (prev == null) {
            table.deleteShoppingList(newEntry);
            throw new IllegalStateException("Tried to update not existing list with id '" + newEntry.getId() + "'.");
        }
        return newEntry;
    }

    private boolean isShoppingListOwner(User user, ShoppingList shoppingList) {
        return user.getShoppingLists().contains(shoppingList.getId());
    }
}
