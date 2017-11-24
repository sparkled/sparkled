package net.chrisparton.sparkled.schema.inject;

import com.google.inject.AbstractModule;
import net.chrisparton.sparkled.schema.SchemaUpdater;
import net.chrisparton.sparkled.schema.SchemaUpdaterImpl;

public class SchemaModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SchemaUpdater.class).to(SchemaUpdaterImpl.class).asEagerSingleton();
    }
}
