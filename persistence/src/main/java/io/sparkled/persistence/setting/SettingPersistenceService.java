package io.sparkled.persistence.setting;

import io.sparkled.model.setting.SettingsCache;

public interface SettingPersistenceService {

    SettingsCache getSettings();
}
