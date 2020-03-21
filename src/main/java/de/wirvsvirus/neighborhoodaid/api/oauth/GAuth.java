package de.wirvsvirus.neighborhoodaid.api.oauth;

import de.wirvsvirus.neighborhoodaid.api.Endpoint;
import de.wirvsvirus.neighborhoodaid.api.utils.RestUtils;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.providers.GoogleAuth;
import io.vertx.ext.web.Router;
import org.jetbrains.annotations.NotNull;

public class GAuth implements Endpoint {

    private final String redirectUri = "http://127.0.0.1:8080/oauth";

    @Override
    public void setupRouting(@NotNull Vertx vertx, @NotNull Router router) {

        OAuth2Auth authProvider = GoogleAuth.create(vertx, "911847919177-tlcbi4vctr05b8jmkolhqrnh144v56rk.apps.googleusercontent.com", "Y8pOsMHUpoW1vQ44Z98eNA9B");

        router.get().handler(ctx -> {
            final var code = ctx.request().getParam("code");
            if (code == null) {
                JsonObject body = new JsonObject()
                        .put("redirect_uri", redirectUri)
                        .put("scope", "email profile")
                        .put("state", "")
                        .put("prompt", "consent");
                final var loginUrl = authProvider.authorizeURL(body);
                ctx.response().putHeader("Location", loginUrl)
                        .setStatusCode(302)
                        .end();
            } else {
                JsonObject body = new JsonObject()
                        .put("code", code)
                        .put("redirect_uri", redirectUri);
                authProvider.authenticate(body, res -> {
                    if (res.succeeded()) {
                        final var user = res.result();
                        //TODO User handling, contains access token
                        RestUtils.endResponseWithHtmlSuccess(ctx, 200, "OAuth successful granted.");
                    } else {
                        RestUtils.endResponseWithError(ctx, 500, res.cause().getLocalizedMessage());
                    }
                });
            }
        });
    }


}

