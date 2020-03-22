package de.wirvsvirus.neighborhoodaid.db.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private final UUID id;
    private final String name;
    private final String email;
    private final String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private final String phoneNumber;
    private final Address address;
    private final List<UUID> shoppingLists;

    public User(UUID id, String name, String email, String password, String phoneNumber, Address address,
                List<UUID> shoppingLists) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.shoppingLists = shoppingLists;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public List<UUID> getShoppingLists() {
        return shoppingLists;
    }
}
