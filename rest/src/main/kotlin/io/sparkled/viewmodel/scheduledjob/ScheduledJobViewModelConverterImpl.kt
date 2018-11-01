package io.sparkled.viewmodel.scheduledjob

import io.sparkled.model.entity.ScheduledJob
import io.sparkled.persistence.scheduledjob.ScheduledJobPersistenceService
import io.sparkled.viewmodel.exception.ViewModelConversionException
import javax.inject.Inject

class ScheduledJobViewModelConverterImpl
@Inject constructor(private val scheduledJobPersistenceService: ScheduledJobPersistenceService) :
    ScheduledJobViewModelConverter() {

    override fun toViewModel(model: ScheduledJob): ScheduledJobViewModel {
        return ScheduledJobViewModel(
            id = model.id,
            action = model.action,
            cronExpression = model.cronExpression,
            value = model.value,
            playlistId = model.playlistId
        )
    }

    override fun toModel(viewModel: ScheduledJobViewModel): ScheduledJob {
        val scheduledJobId = viewModel.id
        val model = getScheduledJob(scheduledJobId)

        return with(model) {
            cronExpression = viewModel.cronExpression
            action = viewModel.action
            value = viewModel.value
            playlistId = viewModel.playlistId

            return@with model
        }
    }

    private fun getScheduledJob(scheduledJobId: Int?): ScheduledJob {
        if (scheduledJobId == null) {
            return ScheduledJob()
        }

        return scheduledJobPersistenceService.getScheduledJobById(scheduledJobId)
            ?: throw ViewModelConversionException("Scheduled job with ID of '$scheduledJobId' not found.")
    }
}
