package de.wirvsvirus.neighborhoodaid.api.stats;

import de.wirvsvirus.neighborhoodaid.api.Endpoint;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;

public class HealthEndpoint implements Endpoint {

    @Override
    public void setupRouting(@NotNull final Vertx vertx, @NotNull final Router router) {
        router.get().handler(ctx -> {
            final var uptime = ManagementFactory.getRuntimeMXBean().getUptime();
            final var formatted = DurationFormatUtils.formatDurationHMS(uptime);
            final var json = new JsonObject();
            json.put("millis", uptime).put("formatted", formatted);
            ctx.response().end(new JsonObject().put("uptime", json).encodePrettily());
        });
    }
}
