package io.sparkled.persistence.setting

import io.sparkled.model.setting.SettingsCache

interface SettingPersistenceService {

    val settings: SettingsCache
}
