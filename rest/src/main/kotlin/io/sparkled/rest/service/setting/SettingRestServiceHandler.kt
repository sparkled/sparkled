package io.sparkled.rest.service.setting

import io.sparkled.model.entity.Setting
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.persistence.setting.SettingPersistenceService
import io.sparkled.rest.service.RestServiceHandler
import javax.inject.Inject
import javax.ws.rs.core.Response

class SettingRestServiceHandler
@Inject constructor(
    private val settingPersistenceService: SettingPersistenceService
) : RestServiceHandler() {

    fun getAllSettings(): Response {
        return respondOk(listOf(getBrightnessSetting()))
    }

    fun getSetting(code: String): Response {
        return if (code == SettingsConstants.Brightness.CODE) {
            val setting = getBrightnessSetting()
            respondOk(setting)
        } else {
            respond(Response.Status.NOT_FOUND, "Setting not found.")
        }
    }

    private fun getBrightnessSetting(): Setting {
        return Setting()
            .setCode(SettingsConstants.Brightness.CODE)
            .setValue(settingPersistenceService.settings.brightness.toString())
    }

    fun updateSetting(code: String, setting: Setting): Response {
        setting.setCode(code)
        settingPersistenceService.setBrightness(setting.getValue() ?: "")
        return respondOk()
    }
}