package net.chrisparton.sparkled.schema;

public interface SchemaUpdater {

    /**
     * Applies changes to the database schema.
     */
    void update() throws Exception;
}
