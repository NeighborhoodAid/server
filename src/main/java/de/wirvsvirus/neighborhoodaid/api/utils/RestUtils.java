package de.wirvsvirus.neighborhoodaid.api.utils;

import de.wirvsvirus.neighborhoodaid.api.ErrorResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class RestUtils {

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
}
