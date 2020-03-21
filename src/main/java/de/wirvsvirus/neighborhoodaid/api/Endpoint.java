package de.wirvsvirus.neighborhoodaid.api;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.jetbrains.annotations.NotNull;

public interface Endpoint {

    void setupRouting(@NotNull final Vertx vertx, @NotNull final Router subRouter);

}
