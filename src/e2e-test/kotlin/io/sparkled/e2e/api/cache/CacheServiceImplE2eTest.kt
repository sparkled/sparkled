package io.sparkled.e2e.api.cache

import io.sparkled.e2e.spec.E2eSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.sparkled.model.entity.v2.SettingEntity
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.persistence.cache.Cache
import io.sparkled.persistence.insert
import io.sparkled.persistence.update
import org.jdbi.v3.core.Jdbi
import kotlin.reflect.full.memberProperties

class CacheServiceImplE2eTest : E2eSpec() {

    init {
        "All caches are registered in allCaches list" {
            val allCaches = caches.allCaches
            val cacheFields = caches::class.memberProperties.filter {
                it.returnType.classifier == Cache::class
            }.map { it.getter.call(caches) }

            allCaches shouldContainExactlyInAnyOrder cacheFields
        }

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
