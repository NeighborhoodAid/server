package de.wirvsvirus.neighborhoodaid.db;

import one.microstream.storage.types.EmbeddedStorageManager;

public class DbAccessor<T> {

    private final EmbeddedStorageManager storageManager;
    private final T rootObj;

    public DbAccessor(final EmbeddedStorageManager storageManager, final T rootObj) {
        this.storageManager = storageManager;
        this.rootObj = rootObj;
    }

    public EmbeddedStorageManager getStorageManager() {
        return storageManager;
    }

    public T getRoot() {
        return rootObj;
    }

    public void store() {
        storageManager.storeRoot();
    }

}
