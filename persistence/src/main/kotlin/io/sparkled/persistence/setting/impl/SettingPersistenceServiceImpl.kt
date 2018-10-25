package io.sparkled.persistence.setting.impl

import io.sparkled.model.entity.Setting
import io.sparkled.model.setting.SettingsCache
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.setting.SettingPersistenceService
import io.sparkled.persistence.setting.impl.query.GetSettingsQuery
import javax.inject.Inject

class SettingPersistenceServiceImpl @Inject
constructor(private val queryFactory: QueryFactory) : SettingPersistenceService {
    private var settingsCache: SettingsCache? = null

    override val settings: SettingsCache
        get() {
            if (settingsCache == null) {
                val settings = GetSettingsQuery().perform(queryFactory)
                val settingsMap = settings.associateBy({ it.getCode()!! }, { it })
                settingsCache = SettingsCache(getBrightness(settingsMap))
            }

            return settingsCache!!
        }

    private fun getBrightness(settingsMap: Map<String, Setting>): Int {
        val setting = settingsMap.getOrDefault(SettingsConstants.BRIGHTNESS.CODE, Setting())

        return try {
            val brightness = Integer.parseInt(setting.getValue())
            constrain(brightness, SettingsConstants.BRIGHTNESS.MIN, SettingsConstants.BRIGHTNESS.MAX)
        } catch (e: NumberFormatException) {
            SettingsConstants.BRIGHTNESS.MAX
        }
    }

    private fun constrain(value: Int, min: Int, max: Int): Int {
        return Math.max(min, Math.min(max, value))
    }
}
