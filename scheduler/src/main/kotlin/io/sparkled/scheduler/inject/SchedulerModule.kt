package io.sparkled.scheduler.inject

import com.google.inject.AbstractModule
import io.sparkled.scheduler.SchedulerService
import io.sparkled.scheduler.impl.SchedulerServiceImpl
import org.slf4j.LoggerFactory

class SchedulerModule : AbstractModule() {

    override fun configure() {
        logger.info("Configuring Guice module.")
        bind(SchedulerService::class.java).to(SchedulerServiceImpl::class.java).asEagerSingleton()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SchedulerModule::class.java)
    }
}
