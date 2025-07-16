package org.example.migration;

public abstract class AbstractMigration implements Migration {
    private Migration nextMigration;

    @Override
    public void migrate() {
        migrateInternal();
        if (nextMigration != null) {
            nextMigration.migrate();
        }
    }

    @Override
    public void setNext(Migration migration) {
        this.nextMigration = migration;
    }

    protected abstract void migrateInternal();
}
