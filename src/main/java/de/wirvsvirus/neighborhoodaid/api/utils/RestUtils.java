package de.wirvsvirus.neighborhoodaid.api.utils;

import de.wirvsvirus.neighborhoodaid.api.ErrorResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class RestUtils {

    public static void endResponseWithError(final RoutingContext ctx, final int statusCode, final String errorMessage) {
        ctx.response().setStatusCode(statusCode).end(Json.encodePrettily(new ErrorResponse(statusCode, errorMessage)));
    }
}
