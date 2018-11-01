package io.sparkled.viewmodel.scheduledjob.search

import io.sparkled.model.entity.ScheduledJob
import io.sparkled.viewmodel.ModelCollectionConverter

abstract class ScheduledJobSearchViewModelConverter :
    ModelCollectionConverter<ScheduledJob, ScheduledJobSearchViewModel>
