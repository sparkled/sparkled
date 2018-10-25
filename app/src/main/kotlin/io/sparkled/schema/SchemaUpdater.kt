package io.sparkled.schema

interface SchemaUpdater {

    /**
     * Applies changes to the database schema.
     */
    @Throws(Exception::class)
    fun update()
}
