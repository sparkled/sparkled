package io.sparkled.rest.service.scheduledjob

import io.sparkled.persistence.scheduledjob.ScheduledJobPersistenceService
import io.sparkled.persistence.transaction.Transaction
import io.sparkled.rest.response.IdResponse
import io.sparkled.rest.service.RestServiceHandler
import io.sparkled.scheduler.SchedulerService
import io.sparkled.viewmodel.scheduledjob.ScheduledJobViewModel
import io.sparkled.viewmodel.scheduledjob.ScheduledJobViewModelConverter
import io.sparkled.viewmodel.scheduledjob.search.ScheduledJobSearchViewModelConverter
import javax.inject.Inject
import javax.ws.rs.core.Response

class ScheduledJobRestServiceHandler
@Inject constructor(
    private val transaction: Transaction,
    private val scheduledJobPersistenceService: ScheduledJobPersistenceService,
    private val scheduledJobViewModelConverter: ScheduledJobViewModelConverter,
    private val scheduledJobSearchViewModelConverter: ScheduledJobSearchViewModelConverter,
    private val schedulerService: SchedulerService
) : RestServiceHandler() {

    internal fun createScheduledJob(scheduledJobViewModel: ScheduledJobViewModel): Response {
        return transaction.of {
            var scheduledJob = scheduledJobViewModelConverter.toModel(scheduledJobViewModel)
            scheduledJob = scheduledJobPersistenceService.createScheduledJob(scheduledJob)

            schedulerService.reload()
            return@of respondOk(IdResponse(scheduledJob.id!!))
        }
    }

    internal fun getAllScheduledJobs(): Response {
        val scheduledJobs = scheduledJobPersistenceService.getAllScheduledJobs()
        val results = scheduledJobSearchViewModelConverter.toViewModels(scheduledJobs)
        return respondOk(results)
    }

    internal fun getScheduledJob(scheduledJobId: Int): Response {
        val scheduledJob = scheduledJobPersistenceService.getScheduledJobById(scheduledJobId)

        if (scheduledJob != null) {
            val viewModel = scheduledJobViewModelConverter.toViewModel(scheduledJob)
            return respondOk(viewModel)
        }

        return respond(Response.Status.NOT_FOUND, "Scheduled job not found.")
    }

    internal fun deleteScheduledJob(id: Int): Response {
        return transaction.of {
            scheduledJobPersistenceService.deleteScheduledJob(id)

            schedulerService.reload()
            return@of respondOk()
        }
    }
}