package io.sparkled.api

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.sparkled.model.SettingModel
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.persistence.DbService
import io.sparkled.persistence.cache.CacheService
import io.sparkled.viewmodel.SettingViewModel
import jakarta.transaction.Transactional

@ExecuteOn(TaskExecutors.BLOCKING)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/settings")
class SettingController(
    private val caches: CacheService,
    private val db: DbService,
) {

    @Get("/")
    @Transactional
    fun getAllSettings(): HttpResponse<Any> {
        val settings = caches.settings.get()
        return HttpResponse.ok(settings)
    }

    @Get("/{code}")
    @Transactional
    fun getSetting(code: String): HttpResponse<Any> {
        return if (code == SettingsConstants.Brightness.CODE) {
            val setting = SettingModel(
                code = SettingsConstants.Brightness.CODE,
                value = caches.settings.use { it.brightness }.toString()
            )
            HttpResponse.ok(SettingViewModel.fromModel(setting))
        } else {
            return HttpResponse.notFound()
        }
    }

    @Put("/{code}")
    @Transactional
    fun updateSetting(code: String, setting: SettingViewModel): HttpResponse<Any> {
        val model = setting.toModel()

        if (setting.code == SettingsConstants.Brightness.CODE) {
            try {
                db.settings.save(model)
            } catch (e: Exception) {
                db.settings.update(model)
            }

            caches.settings.modify { it.copy(brightness = model.value.toInt()) }
        } else {
            return HttpResponse.notFound()
        }

        return HttpResponse.noContent()
    }
}
