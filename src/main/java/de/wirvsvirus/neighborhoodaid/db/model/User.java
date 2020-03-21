package de.wirvsvirus.neighborhoodaid.db.model;

import java.util.List;
import java.util.UUID;

public class User {

    private final UUID id;
    private final String name;
    private final String email;
    private final String password;
    private final String phoneNumber;
    private final Address address;
    private final List<Integer> shoppingLists;

    public User(UUID id, String name, String email, String password, String phoneNumber, Address address, List<Integer> shoppingLists) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.shoppingLists = shoppingLists;
    }


}