package io.sparkled.rest

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.sparkled.model.animation.easing.EditorItem
import io.sparkled.model.animation.fill.BlendMode
import io.sparkled.renderer.SparkledPluginManager

@ExecuteOn(TaskExecutors.IO)
@Controller("/api/referenceData")
open class ReferenceDataController(
    private val pluginManager: SparkledPluginManager
) {

    @Get("/")
    open fun getAllReferenceData(): HttpResponse<Any> {
        val data = ReferenceDataResponse(
            blendModes = BlendMode.values().map { EditorItem(it.name, it.displayName) },
            easings = pluginManager.easings.get().values.map { EditorItem(it.id, it.name, it.params) },
            effects = pluginManager.effects.get().values.map { EditorItem(it.id, it.name, it.params) },
            fills = pluginManager.fills.get().values.map { EditorItem(it.id, it.name, it.params) },
        )

        return HttpResponse.ok(data)
    }
}

data class ReferenceDataResponse(
    val blendModes: List<EditorItem>,
    val easings: List<EditorItem>,
    val effects: List<EditorItem>,
    val fills: List<EditorItem>
)
