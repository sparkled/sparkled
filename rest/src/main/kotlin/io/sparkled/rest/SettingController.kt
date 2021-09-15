package io.sparkled.rest

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.sparkled.model.entity.v2.SettingEntity
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.persistence.DbService
import io.sparkled.persistence.cache.CacheService
import io.sparkled.persistence.insert
import io.sparkled.persistence.update
import io.sparkled.viewmodel.SettingViewModel
import org.springframework.transaction.annotation.Transactional

@ExecuteOn(TaskExecutors.IO)
@Controller("/api/settings")
open class SettingController(
    private val caches: CacheService,
    private val db: DbService
) {

    @Get("/")
    @Transactional(readOnly = true)
    open fun getAllSettings(): HttpResponse<Any> {
        val settings = caches.settings.get()
        return HttpResponse.ok(settings)
    }

    @Get("/{code}")
    @Transactional(readOnly = true)
    open fun getSetting(code: String): HttpResponse<Any> {
        return if (code == SettingsConstants.Brightness.CODE) {
            val setting = SettingEntity(
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
    open fun updateSetting(code: String, setting: SettingViewModel): HttpResponse<Any> {
        val model = setting.toModel()

        if (setting.code == SettingsConstants.Brightness.CODE) {
            try {
                db.insert(model)
            } catch (e: Exception) {
                db.update(model)
            }

            caches.settings.modify { it.copy(brightness = model.value.toInt()) }
        } else {
            return HttpResponse.notFound()
        }

        return HttpResponse.ok()
    }
}
