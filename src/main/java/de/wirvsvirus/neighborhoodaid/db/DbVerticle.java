package de.wirvsvirus.neighborhoodaid.db;

import de.wirvsvirus.neighborhoodaid.VertxBusAddresses;
import de.wirvsvirus.neighborhoodaid.db.model.DbRoot;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import one.microstream.storage.types.EmbeddedStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(DbVerticle.class);

    private DbAccessor<DbRoot> accessor;

    @Override
    public void start(Promise<Void> startPromise) {
        logger.info("Starting database");
        final var root = new DbRoot();
        final var storageManager = EmbeddedStorage.start(root);
        this.accessor = new DbAccessor<>(storageManager, root);
        vertx.eventBus().<String>consumer(VertxBusAddresses.DB_ROOT, msg -> {
            final var cmd = msg.body();
            switch (cmd){
                case DbCommands.GET_ACCESSOR:
                    msg.reply(accessor);
                    break;
                case DbCommands.SAVE_ROOT:
                    this.accessor.store();
                    break;
                default:
                    logger.warn("Unknown database command '" + cmd + "'!");
            }
            msg.reply(root);
        });
        logger.info("Database running.");
        startPromise.complete();
    }
}
