package io.sparkled.schema.inject;

import com.google.inject.AbstractModule;
import io.sparkled.schema.SchemaUpdater;
import io.sparkled.schema.SchemaUpdaterImpl;

public class SchemaModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SchemaUpdater.class).to(SchemaUpdaterImpl.class).asEagerSingleton();
    }
}
