package io.sparkled.rest

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.spring.tx.annotation.Transactional
import io.sparkled.model.entity.Setting
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.persistence.setting.SettingPersistenceService

@Controller("/rest/settings")
open class SettingController(
    private val settingPersistenceService: SettingPersistenceService
) {

    @Get("/")
    @Transactional(readOnly = true)
    open fun getAllSettings(): HttpResponse<Any> {
        val settings = settingPersistenceService.settings
        return HttpResponse.ok(settings)
    }

    @Get("/{code}")
    @Transactional(readOnly = true)
    open fun getSetting(code: String): HttpResponse<Any> {
        return if (code == SettingsConstants.Brightness.CODE) {
            val setting = Setting()
                .setCode(SettingsConstants.Brightness.CODE)
                .setValue(settingPersistenceService.settings.brightness.toString())
            HttpResponse.ok(setting)
        } else {
            return HttpResponse.notFound()
        }
    }

    @Put("/{code}")
    @Transactional
    open fun updateSetting(code: String, setting: Setting): HttpResponse<Any> {
        settingPersistenceService.setBrightness(setting.getValue() ?: "0")
        return HttpResponse.ok()
    }
}
