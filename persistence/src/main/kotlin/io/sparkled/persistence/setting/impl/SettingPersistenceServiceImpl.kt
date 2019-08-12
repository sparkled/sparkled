package io.sparkled.persistence.setting.impl

import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.sparkled.model.entity.Setting
import io.sparkled.model.setting.SettingsCache
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.model.util.MathUtils.constrain
import io.sparkled.persistence.QueryFactory
import io.sparkled.persistence.setting.SettingPersistenceService
import io.sparkled.persistence.setting.impl.query.GetSettingsQuery
import io.sparkled.persistence.setting.impl.query.SaveSettingQuery
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Singleton

@Singleton
class SettingPersistenceServiceImpl(private val queryFactory: QueryFactory) : SettingPersistenceService {

    private var settingsCache: SettingsCache? = null

    private var executor: ExecutorService = Executors.newSingleThreadExecutor(
        ThreadFactoryBuilder().setNameFormat("settings-cache-%d").build()
    )

    override val settings: SettingsCache
        get() {
            if (settingsCache == null) {
                reloadSettingsCache()
            }

            return settingsCache!!
        }

    /**
     * The settings cache is read and written to by multiple threads (e.g. REST API updates brightness, UDP server reads
     * new brightness). Therefore, the entity manager is cleared to ensure fresh data is retrieved from the database.
     */
    private fun reloadSettingsCache() {
        queryFactory.entityManager.clear()

        val settings = GetSettingsQuery().perform(queryFactory)
        val settingsMap = settings.associateBy({ it.getCode()!! }, { it })
        settingsCache = SettingsCache(getBrightness(settingsMap))
    }

    override fun setBrightness(brightness: String) {
        val setting = Setting().setCode(SettingsConstants.Brightness.CODE).setValue(brightness)
        SaveSettingQuery(setting).perform(queryFactory)
        settingsCache = SettingsCache(brightness.toInt())
    }

    private fun getBrightness(settingsMap: Map<String, Setting>): Int {
        val setting = settingsMap.getOrDefault(SettingsConstants.Brightness.CODE, Setting())

        return try {
            val brightness = Integer.parseInt(setting.getValue())
            constrain(brightness, SettingsConstants.Brightness.MIN, SettingsConstants.Brightness.MAX)
        } catch (e: NumberFormatException) {
            SettingsConstants.Brightness.MAX
        }
    }
}
