package io.sparkled.schema

interface SchemaUpdater {

    /**
     * Applies changes to the database schema.
     */
    fun update()
}
