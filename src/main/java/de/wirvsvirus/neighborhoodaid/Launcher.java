package de.wirvsvirus.neighborhoodaid;

import org.slf4j.LoggerFactory;

import io.sentry.Sentry;

/**
 * Hello world!
 *
 */
public class Launcher {
    public static void main(String[] args) {
        if (System.getenv("SENTRY_DSN") != null || System.getProperty("sentry.properties") != null) {
            Sentry.init();
        }
        try {
            new Server();
        } catch (Exception exception) {
            LoggerFactory.getLogger(Launcher.class)
                    .error("Encountered exception while initializing the server application!", exception);
            System.exit(1);
        }
    }
}
