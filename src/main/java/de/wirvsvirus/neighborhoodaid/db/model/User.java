package de.wirvsvirus.neighborhoodaid.db.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class User {

    private final UUID id;
    private final String name;
    private final String email;
    @JsonProperty(access = Access.WRITE_ONLY)
    private final String password;
    private final String phoneNumber;
    private final Address address;
    private final List<Integer> shoppingLists;

    public User(UUID id, String name, String email, String password, String phoneNumber, Address address,
            List<Integer> shoppingLists) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.shoppingLists = shoppingLists;
    }

    public Address getAddress() {
        return address;
    }

    public List<Integer> getShoppingLists() {
        return shoppingLists;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

}
