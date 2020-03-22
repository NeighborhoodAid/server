package de.wirvsvirus.neighborhoodaid.db.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    public static class Login {
        private final LoginType type;
        private final String data;

        public enum LoginType {
            GAUTH, EMAIL;
        }

        public Login(LoginType type, String data){
            this.type = type;
            this.data = data;
        }

        public LoginType getType() {
            return type;
        }

        public String getData() {
            return data;
        }
        public static Login email(String email){
            return new Login(LoginType.EMAIL, email);
        }
        public static Login gauth(String uid){
            return new Login(LoginType.GAUTH, uid);
        }
    }

    private final UUID id;
    private final String name;
    private final Login login;
    private final String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private final String phoneNumber;
    private final Address address;
    private final List<UUID> shoppingLists;

    public User(UUID id, String name, Login login, String password, String phoneNumber, Address address,
                List<UUID> shoppingLists) {
        this.id = id;
        this.name = name;
        this.login = login;
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

    public Login getLogin() {
        return login;
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
