package io.sparkled.e2e.api.cache

import io.kotest.matchers.shouldBe
import io.sparkled.e2e.spec.E2eSpec
import io.sparkled.model.SettingModel
import io.sparkled.model.setting.SettingsConstants

class CacheServiceImplE2eTest : E2eSpec() {

    init {
        "Settings are cached" {
            val setting = db.settings.save(
                SettingModel(
                    code = SettingsConstants.Brightness.CODE,
                    value = "10",
                )
            )

            caches.settings.clear()
            caches.settings.get().apply {
                brightness shouldBe 10
            }

            db.settings.update(setting.copy(value = "20"))

            caches.settings.clear()
            caches.settings.get().apply {
                brightness shouldBe 20
            }
        }
    }
}
