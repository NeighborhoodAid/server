package de.wirvsvirus.neighborhoodaid.db.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class User {

    private final UUID id;
    private final String name;
    private final String email;
    private final String password;
    private final String phoneNumber;
    private final Address address;
    private final List<UUID> shoppingLists;

    @JsonCreator
    public User(@JsonProperty("id") UUID id, @JsonProperty("name") String name, @JsonProperty("email") String email,
                @JsonProperty("password") String password, @JsonProperty("phoneNumber") String phoneNumber,
                @JsonProperty("address") Address address,
                @JsonProperty("shoppingLists") List<UUID> shoppingLists) {
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

    @JsonIgnore
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

    public User withUpdate(User user) {
        return new User(this.id, user.name, user.email, this.password, user.phoneNumber, user.address, this.shoppingLists);
    }

    public User withNewAddress(Address address) {
        return new User(this.id, this.name, this.email, this.password, this.phoneNumber, address, this.shoppingLists);
    }
}
