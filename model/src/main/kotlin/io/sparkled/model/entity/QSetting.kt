package io.sparkled.model.entity

import com.querydsl.core.types.PathMetadataFactory.forVariable
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.StringPath

/**
 * QSetting is a Querydsl query type for Setting
 */
class QSetting(variable: String) : EntityPathBase<Setting>(Setting::class.java, forVariable(variable)) {

    val code: StringPath = createString("code")
    val value: StringPath = createString("value")

    companion object {
        val setting = QSetting("setting")
    }
}
