package io.sparkled.e2e.api.cache

import io.kotest.matchers.shouldBe
import io.sparkled.e2e.spec.E2eSpec
import io.sparkled.model.entity.v2.SettingEntity
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.persistence.update
import org.jdbi.v3.core.Jdbi

class CacheServiceImplE2eTest : E2eSpec() {

    init {
        "Settings are cached" {
            inject<Jdbi>().inTransaction<Any, Exception> {
                db.update(SettingEntity(code = SettingsConstants.Brightness.CODE, value = "10"))
            }

            caches.settings.clear()
            caches.settings.get().apply {
                brightness shouldBe 10
            }

            db.update(SettingEntity(code = SettingsConstants.Brightness.CODE, value = "100"))

            caches.settings.clear()
            caches.settings.get().apply {
                brightness shouldBe 100
            }
        }
    }
}
