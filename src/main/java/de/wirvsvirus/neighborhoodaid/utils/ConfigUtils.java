package de.wirvsvirus.neighborhoodaid.utils;

import io.vertx.core.json.JsonObject;

public class ConfigUtils {

    public static int getHttpPort(final JsonObject json) {
        final var env = json.getJsonObject("env");
        if (env != null) {
            return env.getInteger("http_port", 8080);
        }
        return 8080;
    }

    public static boolean isDevModeActive(final JsonObject json) {
        final var env = json.getJsonObject("env");
        if (env != null) {
            return env.getBoolean("dev_mode", Boolean.FALSE);
        }
        return false;
    }

}
