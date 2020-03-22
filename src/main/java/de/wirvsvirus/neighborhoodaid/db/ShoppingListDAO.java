package de.wirvsvirus.neighborhoodaid.db;

import de.wirvsvirus.neighborhoodaid.db.model.Address;
import de.wirvsvirus.neighborhoodaid.db.model.DataRoot;
import de.wirvsvirus.neighborhoodaid.db.model.ShoppingList;
import de.wirvsvirus.neighborhoodaid.db.model.User;
import de.wirvsvirus.neighborhoodaid.geolocation.GeoLocation;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public List<ShoppingList> getShoppingListsNear(Address address, double radius) {
        return accessor.getRoot().getShoppingLists().values().stream()
                .sorted(Comparator.comparingDouble(this::getDistanceOfShoppingListToAdress))
                .takeWhile(shoppingList -> getDistanceOfShoppingListToAdress(shoppingList) <= radius).collect(Collectors.toList());
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
        return user.getShoppingLists().contains(shoppingList.getId());
    }

    private double getDistanceOfShoppingListToAdress(ShoppingList shoppingList) {
        return GeoLocation.sphericalDistance(
                Address.empty(),
                accessor.getRoot().getUsers().get(
                        shoppingList.getCreator()
                ).getAddress());
    }
}
