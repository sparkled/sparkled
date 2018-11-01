package io.sparkled.persistence.setting.impl.query

import io.sparkled.model.entity.Setting
import io.sparkled.model.validator.SettingValidator
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import org.slf4j.LoggerFactory

class SaveSettingQuery(private val setting: Setting) : PersistenceQuery<Setting> {

    override fun perform(queryFactory: QueryFactory): Setting {
        SettingValidator().validate(setting)

        val entityManager = queryFactory.entityManager
        val savedSetting = entityManager.merge(setting)
        logger.info("Saved setting {} ({}).", savedSetting.getCode(), savedSetting.getValue())
        return savedSetting!!
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SaveSettingQuery::class.java)
    }
}
