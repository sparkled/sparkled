package io.sparkled.api

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.sparkled.model.animation.fill.BlendMode
import io.sparkled.model.animation.param.Param
import io.sparkled.renderer.SparkledPluginManager
import io.sparkled.renderer.api.SparkledPlugin
import io.sparkled.renderer.parameter.ParameterFn
import io.sparkled.viewmodel.EditorItemViewModel
import io.sparkled.viewmodel.ReferenceDataViewModel
import java.awt.Color
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@ExecuteOn(TaskExecutors.BLOCKING)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/reference-data")
class ReferenceDataController(
    private val pluginManager: SparkledPluginManager,
) {

    @Get("/")
    fun getAllReferenceData(): HttpResponse<ReferenceDataViewModel> {
        val data = ReferenceDataViewModel(
            blendModes = BlendMode.entries.map { EditorItemViewModel(it.name, it.displayName) },
            easings = pluginManager.easings.get().values.map { EditorItemViewModel(it.id, it.name, getParams(it)) },
            effects = pluginManager.effects.get().values.map { EditorItemViewModel(it.id, it.name, getParams(it)) },
            fills = pluginManager.fills.get().values.map { EditorItemViewModel(it.id, it.name, getParams(it)) },
        )

        return HttpResponse.ok(data)
    }

    private fun getParams(plugin: SparkledPlugin): List<Param> {
        return plugin::class.memberProperties
            .filter { it.returnType.classifier == ParameterFn::class }
            .map { parameterFn ->
                @Suppress("UNCHECKED_CAST")
                val value = (parameterFn as KProperty1<SparkledPlugin, ParameterFn<SparkledPlugin>>)
                    .apply { isAccessible = true }
                    .get(plugin) as ParameterFn<Any>
                Param(
                    code = parameterFn.name,
                    displayName = value.displayName,
                    type = value.type,
                    defaultValue = listOfNotNull(value.defaultValue).map {
                        if (it is Color) "#" + Integer.toHexString(it.rgb).drop(2) else it.toString()
                    },
                )
            }
    }
}
