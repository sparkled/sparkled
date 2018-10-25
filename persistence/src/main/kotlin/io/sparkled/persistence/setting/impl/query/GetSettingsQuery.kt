package io.sparkled.persistence.setting.impl.query

import io.sparkled.model.entity.Setting
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetSettingsQuery : PersistenceQuery<List<Setting>> {

    @Override
    fun perform(queryFactory: QueryFactory): List<Setting> {
        return queryFactory
                .selectFrom(qSetting)
                .fetch()
    }
}
