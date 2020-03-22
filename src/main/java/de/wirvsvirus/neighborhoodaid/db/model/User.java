package de.wirvsvirus.neighborhoodaid.db.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private final UUID id;
    private final Login login;
    private final String name;
    private final String email;
    private final String password;
    private final String phoneNumber;
    private final Address address;
    private final List<UUID> shoppingLists;

    @JsonCreator
    public User(@JsonProperty("id") UUID id, @JsonProperty("login") Login login, @JsonProperty("name") String name,
                @JsonProperty("email") String email, @JsonProperty("password") String password,
                @JsonProperty("phoneNumber") String phoneNumber,
                @JsonProperty("address") Address address,
                @JsonProperty("shoppingLists") List<UUID> shoppingLists) {
        this.id = id;
        this.login = login;
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

    public Login getLogin() {
        return login;
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

    public User withNewIdAndPassword(final UUID uuid, final String hashedPassword) {
        return new User(uuid, this.login, this.name, email, hashedPassword, this.phoneNumber, this.address, this.shoppingLists);
    }

    public User withUpdate(User user) {
        return new User(this.id, this.login, this.name, email, this.password, user.phoneNumber, user.address, this.shoppingLists);
    }

    public User withNewAddress(Address address) {
        return new User(this.id, this.login, this.name, email, this.password, this.phoneNumber, address, this.shoppingLists);
    }

    public static class Login {
        private final LoginType type;
        private final String id;

        public enum LoginType {
            GAUTH, EMAIL;
        }

        @JsonCreator
        public Login(@JsonProperty("type") LoginType type, @JsonProperty("data") String data) {
            this.type = type;
            this.id = data;
        }

        public LoginType getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public String generateToken(JWTAuth jwt) {
            return jwt.generateToken(new JsonObject()
                    .put("login", asString()));
        }

        public String asString() {
            return type + ":" + id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, type);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (!(obj instanceof Login)) return false;
            Login that = (Login) obj;
            return that.getId().equals(this.getId()) && that.getType() == this.getType();
        }

        public static Login email(String email) {
            return new Login(LoginType.EMAIL, email);
        }

        public static Login gauth(String uid) {
            return new Login(LoginType.GAUTH, uid);
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
    }
}
