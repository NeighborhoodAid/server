package de.wirvsvirus.neighborhoodaid.db.model;

import java.util.HashMap;

public class DbRoot {

        private final HashMap<Integer, User> users = new HashMap<>();
        private final HashMap<Integer, ShoppingList> shoppingLists = new HashMap<>();

}
