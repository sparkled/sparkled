package io.sparkled.schema.inject;

import com.google.inject.AbstractModule;
import io.sparkled.schema.SchemaUpdater;
import io.sparkled.schema.SchemaUpdaterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaModule extends AbstractModule {

    private static final Logger logger = LoggerFactory.getLogger(SchemaModule.class);

    @Override
    protected void configure() {
        logger.info("Configuring Guice module.");

        bind(SchemaUpdater.class).to(SchemaUpdaterImpl.class).asEagerSingleton();
    }
}
