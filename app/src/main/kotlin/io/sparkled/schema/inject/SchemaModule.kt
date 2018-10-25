package io.sparkled.schema.inject

import com.google.inject.AbstractModule
import io.sparkled.schema.SchemaUpdater
import io.sparkled.schema.SchemaUpdaterImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SchemaModule : AbstractModule() {

    @Override
    protected fun configure() {
        logger.info("Configuring Guice module.")

        bind(SchemaUpdater::class.java).to(SchemaUpdaterImpl::class.java).asEagerSingleton()
    }

    companion object {

        private val logger = LoggerFactory.getLogger(SchemaModule::class.java)
    }
}
