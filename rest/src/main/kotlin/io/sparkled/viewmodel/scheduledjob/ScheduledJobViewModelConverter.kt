package io.sparkled.viewmodel.scheduledjob

import io.sparkled.model.entity.ScheduledJob
import io.sparkled.viewmodel.ModelConverter
import io.sparkled.viewmodel.ViewModelConverter

abstract class ScheduledJobViewModelConverter : ModelConverter<ScheduledJob, ScheduledJobViewModel>,
    ViewModelConverter<ScheduledJobViewModel, ScheduledJob>
