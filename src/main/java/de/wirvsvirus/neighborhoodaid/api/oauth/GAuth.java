package de.wirvsvirus.neighborhoodaid.api.oauth;

import de.wirvsvirus.neighborhoodaid.ConfigProperties;
import de.wirvsvirus.neighborhoodaid.api.Endpoint;
import de.wirvsvirus.neighborhoodaid.api.utils.RestUtils;
import de.wirvsvirus.neighborhoodaid.db.model.Address;
import de.wirvsvirus.neighborhoodaid.db.model.User;
import de.wirvsvirus.neighborhoodaid.utils.DbUtils;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.providers.GoogleAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GAuth implements Endpoint {

    private final String redirectUri = "http://127.0.0.1:8080/oauth";
    private final String clientSecret;
    private final String clientId;
    private static final Logger logger = LoggerFactory.getLogger(GAuth.class);
    private final Map<String, String> states = new HashMap<>(); // TODO
    private final JWTAuth jwt;

    public GAuth(JsonObject config, JWTAuth jwt) {
        this.jwt = jwt;
        JsonObject oauthOptions = config.getJsonObject(ConfigProperties.OAUTH);
        clientId = oauthOptions.getString(ConfigProperties.CLIENT_ID);
        clientSecret = oauthOptions.getString(ConfigProperties.CLIENT_SECRET);
    }

    @Override
    public void setupRouting(@NotNull Vertx vertx, @NotNull Router router) {

        OAuth2Auth authProvider = GoogleAuth.create(vertx, clientId, clientSecret);

        router.get().handler(ctx -> {
            final var code = ctx.request().getParam("code");
            if (code == null) {
                redirectToGoogleOAuth(authProvider, ctx);
            } else {
                authenticateWithCode(authProvider, ctx, code);
            }
        });
    }

    private void authenticateWithCode(OAuth2Auth authProvider, RoutingContext ctx, String code) {
        authProvider.authenticate(new JsonObject()
                .put("code", code)
                .put("redirect_uri", redirectUri), res -> {
            if (res.succeeded()) {
                final var user = res.result();
                WebClient.create(ctx.vertx()).get(443, "www.googleapis.com", "/oauth2/v2/userinfo").bearerTokenAuthentication(user.principal().getString("access_token")).ssl(true).send(userInfo -> {
                    if (userInfo.failed()) {
                        logger.error("Error during email acquisition", userInfo.cause());
                    } else {
                        JsonObject userData = userInfo.result().bodyAsJsonObject();
                        User.Login login = User.Login.gauth(userData.getString("id"));
                        DbUtils.getDbAccessor(ctx.vertx(), db -> {
                            Optional<User.Login> dbLogin = db.getRoot().getUsers().values().stream()
                                    .map(User::getLogin)
                                    .filter(login::equals)
                                    .findAny();
                            if (dbLogin.isPresent()) {
                                RestUtils.endResponseWithHtmlSuccess(ctx, 200, "Logged in. Your Token: " + login.generateToken(jwt));
                            } else {
                                final UUID randomUUID = UUID.randomUUID();
                                final User dbUser = new User(randomUUID, "Missing Name", login, "", "", Address.empty(), new ArrayList<>());
                                db.getRoot().getUsers().put(randomUUID, dbUser);
                                RestUtils.endResponseWithHtmlSuccess(ctx, 200, "Signed up. Your Token: " + login.generateToken(jwt));
                            }
                        });
                    }
                });
            } else {
                RestUtils.endResponseWithError(ctx, 500, res.cause().getLocalizedMessage());
            }
        });
    }

    private void redirectToGoogleOAuth(OAuth2Auth authProvider, RoutingContext ctx) {
        final var loginUrl = authProvider.authorizeURL(new JsonObject()
                .put("redirect_uri", redirectUri)
                .put("scope", "email profile")
                .put("state", "")
                .put("prompt", "consent"));
        ctx.response().putHeader("Location", loginUrl)
                .setStatusCode(302)
                .end();
    }


}

