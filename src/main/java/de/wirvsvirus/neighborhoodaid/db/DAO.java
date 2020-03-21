package de.wirvsvirus.neighborhoodaid.db;

public abstract class DAO {

    private final DbAccessor<DbRoot> accessor;

    public DAO(DbAccessor<DbRoot> accessor) {
        this.accessor = accessor;
    }

    protected DbAccessor<DbRoot> getAccessor() {
        return accessor;
    }

    public void save() {
        accessor.store();
    }
}
