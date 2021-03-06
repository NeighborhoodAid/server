package de.wirvsvirus.neighborhoodaid.db;

import de.wirvsvirus.neighborhoodaid.TransformCodec;
import de.wirvsvirus.neighborhoodaid.VertxBusAddresses;
import de.wirvsvirus.neighborhoodaid.db.model.DataRoot;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import one.microstream.storage.types.EmbeddedStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(DbVerticle.class);

    private DbAccessor<DataRoot> accessor;

    @Override
    public void start(Promise<Void> startPromise) {
        logger.info("Starting database");
        final var storageManager = EmbeddedStorage.start(new DataRoot());
        final var root = (DataRoot) storageManager.root();
        final var loadedUsers = root.getUsers().size();
        final var loadedLists = root.getShoppingLists().size();
        logger.info("Loaded users: " + loadedUsers);
        logger.info("Loaded shopping lists: " + loadedLists);
        this.accessor = new DbAccessor<>(storageManager, root);
        vertx.eventBus().registerDefaultCodec(DbAccessor.class, new TransformCodec<>());
        vertx.eventBus().<String>consumer(VertxBusAddresses.DB_ROOT, msg -> {
            final var cmd = msg.body();
            if (DbCommands.GET_ACCESSOR.equals(cmd)) {
                msg.reply(accessor);
            } else {
                logger.warn("Unknown database command '" + cmd + "'!");
                msg.fail(400, "Unknown database command '" + cmd + "'!");
            }
        });
        logger.info("Database running.");
        startPromise.complete();
    }
}
