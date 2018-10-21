package io.sparkled.persistence.setting.impl;

import io.sparkled.model.entity.Setting;
import io.sparkled.model.setting.SettingsCache;
import io.sparkled.model.setting.SettingsConstants;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.setting.SettingPersistenceService;
import io.sparkled.persistence.setting.impl.query.GetSettingsQuery;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SettingPersistenceServiceImpl implements SettingPersistenceService {

    private QueryFactory queryFactory;
    private SettingsCache settingsCache;

    @Inject
    public SettingPersistenceServiceImpl(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public SettingsCache getSettings() {
        if (settingsCache == null) {
            List<Setting> settings = new GetSettingsQuery().perform(queryFactory);
            Map<String, Setting> settingsMap = settings.stream().collect(Collectors.toMap(Setting::getCode, s -> s));
            settingsCache = new SettingsCache(getBrightness(settingsMap));
        }

        return settingsCache;
    }

    private int getBrightness(Map<String, Setting> settingsMap) {
        Setting setting = settingsMap.getOrDefault(SettingsConstants.BRIGHTNESS.CODE, new Setting());

        try {
            int brightness = Integer.parseInt(setting.getValue());
            return constrain(brightness, SettingsConstants.BRIGHTNESS.MIN, SettingsConstants.BRIGHTNESS.MAX);
        } catch (NumberFormatException e) {
            return SettingsConstants.BRIGHTNESS.MAX;
        }
    }

    private int constrain(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
