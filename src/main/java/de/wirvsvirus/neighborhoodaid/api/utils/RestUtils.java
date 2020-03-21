package de.wirvsvirus.neighborhoodaid.api.utils;

import de.wirvsvirus.neighborhoodaid.api.ErrorResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class RestUtils {

    public static void endResponseWithMissingAuthorization(final RoutingContext ctx) {
        endResponseWithError(ctx, 400, "Missing authorization header!");
    }

    public static void endResponseWithInvalidAuthorization(final RoutingContext ctx) {
        endResponseWithInvalidAuthorization(ctx, "Invalid authorization!");
    }

    public static void endResponseWithInvalidAuthorization(final RoutingContext ctx, final String errorMessage) {
        endResponseWithError(ctx, 401, errorMessage);
    }

    public static void endResponseWithError(final RoutingContext ctx, final int statusCode, final String errorMessage) {
        try {
            ctx.response().setStatusCode(statusCode).end(Json.encodePrettily(new ErrorResponse(statusCode, errorMessage)));
        } catch (Throwable e) {
            System.err.println(e);
        }
    }

    public static void endResponseWithHtmlSuccess(final RoutingContext ctx, final int statusCode, final String successMessage) {
        ctx.response().setStatusCode(statusCode).end("<h1 style=\"color:green\")>" + successMessage + "</h1>");
    }

    public static String getAuthorization(final RoutingContext ctx) {
        return ctx.request().getHeader("Authorization");
    }
}