package io.sparkled.persistence.setting.impl

import io.sparkled.model.entity.Setting
import io.sparkled.model.setting.SettingsCache
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.setting.SettingPersistenceService
import io.sparkled.persistence.setting.impl.query.GetSettingsQuery

import javax.inject.Inject
import java.util.stream.Collectors

class SettingPersistenceServiceImpl @Inject
constructor(private val queryFactory: QueryFactory) : SettingPersistenceService {
    private var settingsCache: SettingsCache? = null

    val settings: SettingsCache
        @Override
        get() {
            if (settingsCache == null) {
                val settings = GetSettingsQuery().perform(queryFactory)
                val settingsMap = settings.stream().collect(Collectors.toMap(???({ Setting.getCode() }), { s -> s }))
                settingsCache = SettingsCache(getBrightness(settingsMap))
            }

            return settingsCache
        }

    private fun getBrightness(settingsMap: Map<String, Setting>): Int {
        val setting = settingsMap.getOrDefault(SettingsConstants.BRIGHTNESS.CODE, Setting())

        try {
            val brightness = Integer.parseInt(setting.getValue())
            return constrain(brightness, SettingsConstants.BRIGHTNESS.MIN, SettingsConstants.BRIGHTNESS.MAX)
        } catch (e: NumberFormatException) {
            return SettingsConstants.BRIGHTNESS.MAX
        }

    }

    private fun constrain(value: Int, min: Int, max: Int): Int {
        return Math.max(min, Math.min(max, value))
    }
}
