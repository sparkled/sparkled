package io.sparkled.api

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Put
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.transaction.annotation.Transactional
import io.sparkled.model.SettingModel
import io.sparkled.model.UniqueId
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.persistence.DbService
import io.sparkled.persistence.cache.CacheService
import io.sparkled.persistence.repository.findByIdOrNull
import io.sparkled.viewmodel.SettingEditViewModel
import io.sparkled.viewmodel.SettingViewModel
import io.sparkled.viewmodel.error.ApiErrorCode
import io.sparkled.viewmodel.exception.HttpResponseException
import java.time.Instant

@ExecuteOn(TaskExecutors.BLOCKING)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/settings")
class SettingController(
    private val cache: CacheService,
    private val db: DbService,
) {

    @Get("/")
    @Transactional
    fun getAllSettings(): HttpResponse<Any> {
        val settings = cache.settings.get()
        return HttpResponse.ok(settings)
    }

    @Get("/{id}")
    @Transactional
    fun getSetting(id: UniqueId): HttpResponse<Any> {
        val existing = db.settings.findByIdOrNull(id)
        val setting = when {
            existing != null -> existing

            id == SettingsConstants.Brightness.CODE -> {
                db.settings.save(
                    SettingModel(
                        id = SettingsConstants.Brightness.CODE,
                        value = SettingsConstants.Brightness.MAX.toString()
                    )
                )
            }

            else -> throw HttpResponseException(ApiErrorCode.ERR_NOT_FOUND)
        }

        val viewModel = SettingViewModel(id = setting.id, value = setting.value)
        return HttpResponse.ok(viewModel)
    }

    @Put("/{id}")
    @Transactional
    fun updateSetting(
        @PathVariable id: UniqueId,
        @Body body: SettingEditViewModel,
    ): HttpResponse<Any> {
        val existing = db.settings.findByIdOrNull(id)
        val saved = if (existing == null) {
            db.settings.update(SettingModel(id = id, value = body.value))
        } else {
            db.settings.update(existing.copy(value = body.value, updatedAt = Instant.now()))
        }

        if (id == SettingsConstants.Brightness.CODE) {
            cache.settings.modify { it.copy(brightness = body.value.toInt()) }
        }

        return HttpResponse.ok(
            SettingViewModel(
                id = id,
                value = saved.value,
            )
        )
    }
}
