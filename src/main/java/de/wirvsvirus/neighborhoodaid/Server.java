package de.wirvsvirus.neighborhoodaid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private final Javalin javalin;

    public Server() {
        final long startTime = System.currentTimeMillis();
        logger.info("Starting Server");

        // Disables Jetty Logging
        System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.StdErrLog");
        System.setProperty("org.eclipse.jetty.LEVEL", "OFF");

        javalin = Javalin.create(config -> {

        }).start(7000);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (javalin != null) {
                    javalin.stop();
                }
            }
        });
        logger.info(String.format("Startup finished in %dms!", System.currentTimeMillis() - startTime));
    }
}