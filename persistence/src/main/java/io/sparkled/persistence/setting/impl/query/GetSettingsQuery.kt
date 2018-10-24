package io.sparkled.persistence.setting.impl.query;

import io.sparkled.model.entity.Setting;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.List;

public class GetSettingsQuery implements PersistenceQuery<List<Setting>> {

    @Override
    public List<Setting> perform(QueryFactory queryFactory) {
        return queryFactory
                .selectFrom(qSetting)
                .fetch();
    }
}
