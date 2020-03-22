package de.wirvsvirus.neighborhoodaid.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class User {
    private static final Logger logger = LoggerFactory.getLogger(User.class);

    public static class Login {
        private final LoginType type;
        private final String data;

        public String asString() {
            return type + ":" + data;
        }

        public static Login fromString(String string) {
            String[] split = string.split(":", 2);
            if (split.length < 2) {
                return null;
            }
            try {
                LoginType loginType = LoginType.valueOf(split[0]);
                return new Login(loginType, split[1]);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        public String generateToken(JWTAuth jwt) {
            return jwt.generateToken(new JsonObject()
                    .put("login", asString()));
        }

        public static void fromToken(JWTAuth jwt, String token, Consumer<Login> consumer) {
            jwt.authenticate(new JsonObject()
                    .put("jwt", token), asyncResult -> {
                if (asyncResult.failed()) {
                    logger.error("Error during jwt parsing", asyncResult.cause());
                } else {
                    consumer.accept(fromString(asyncResult.result().principal().getString("login")));
                }
            });
        }

        public enum LoginType {
            GAUTH, EMAIL;
        }

        public Login(LoginType type, String data) {
            this.type = type;
            this.data = data;
        }

        public LoginType getType() {
            return type;
        }

        public String getData() {
            return data;
        }

        public static Login email(String email) {
            return new Login(LoginType.EMAIL, email);
        }

        public static Login gauth(String uid) {
            return new Login(LoginType.GAUTH, uid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(data, type);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (!(obj instanceof Login)) return false;
            Login that = (Login) obj;
            return that.getData().equals(this.getData()) && that.getType() == this.getType();
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
