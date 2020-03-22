package de.wirvsvirus.neighborhoodaid.utils;

import de.wirvsvirus.neighborhoodaid.VertxBusAddresses;
import de.wirvsvirus.neighborhoodaid.db.DbAccessor;
import de.wirvsvirus.neighborhoodaid.db.DbCommands;
import de.wirvsvirus.neighborhoodaid.db.model.DataRoot;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class DbUtils {

    private static final Logger logger = LoggerFactory.getLogger(DbUtils.class);

    public static void getDbAccessor(final Vertx vertx, Consumer<DbAccessor<DataRoot>> consumer) {
        vertx.eventBus().<DbAccessor<DataRoot>>request(VertxBusAddresses.DB_ROOT, DbCommands.GET_ACCESSOR, res -> {
            if (res.succeeded()) {
                consumer.accept(res.result().body());
            } else {
                logger.error("Error requesting DbAccessor!", res.cause());
            }
        });
    }
}
