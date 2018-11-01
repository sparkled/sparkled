package io.sparkled.model.validator

import io.sparkled.model.entity.ScheduledJob
import io.sparkled.model.validator.exception.EntityValidationException

class ScheduledJobValidator {

    fun validate(scheduledJob: ScheduledJob) {
        when {
            scheduledJob.action == null -> throw EntityValidationException(Errors.ACTION_MISSING)
            scheduledJob.cronExpression == null -> throw EntityValidationException(Errors.CRON_EXPRESSION_MISSING)
        }
    }

    private object Errors {
        internal const val ACTION_MISSING = "Scheduled job action must not be empty."
        internal const val CRON_EXPRESSION_MISSING = "Scheduled job configuration must not be empty."
    }
}
