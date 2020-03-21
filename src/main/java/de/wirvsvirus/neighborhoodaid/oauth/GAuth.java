package de.wirvsvirus.neighborhoodaid.oauth;

import de.wirvsvirus.neighborhoodaid.api.Endpoint;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.providers.GoogleAuth;
import io.vertx.ext.web.Router;
import org.jetbrains.annotations.NotNull;

public class GAuth implements Endpoint {


    @Override
    public void setupRouting(@NotNull Vertx vertx, @NotNull Router router) {

        OAuth2Auth authProvider = GoogleAuth.create(vertx, "911847919177-tlcbi4vctr05b8jmkolhqrnh144v56rk.apps.googleusercontent.com", "Y8pOsMHUpoW1vQ44Z98eNA9B");

        JsonObject test = new JsonObject()
                .put("redirect_uri", "http://127.0.0.1:8080/gcallback")
                .put("scope", "email profile")
                .put("state", "")
                .put("prompt", "consent");

        String loginurl = authProvider.authorizeURL(test);

        router.get().handler(ctx -> ctx.response().putHeader("Location",loginurl)
                .setStatusCode(302)
                .end());

        //router.get().handler(ctx -> ctx.response().end(OAuth2AuthHandler.create(authProvider).toString()));
        //router.get().handler();
    }


}

