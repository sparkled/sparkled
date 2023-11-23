package io.sparkled.api

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.sparkled.model.animation.fill.BlendMode
import io.sparkled.renderer.SparkledPluginManager
import io.sparkled.viewmodel.EditorItemViewModel
import io.sparkled.viewmodel.ReferenceDataViewModel

@ExecuteOn(TaskExecutors.BLOCKING)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/api/reference-data")
class ReferenceDataController(
    private val pluginManager: SparkledPluginManager
) {

    @Get("/")
    fun getAllReferenceData(): HttpResponse<ReferenceDataViewModel> {
        val data = ReferenceDataViewModel(
            blendModes = BlendMode.values().map { EditorItemViewModel(it.name, it.displayName) },
            easings = pluginManager.easings.get().values.map { EditorItemViewModel(it.id, it.name, it.params) },
            effects = pluginManager.effects.get().values.map { EditorItemViewModel(it.id, it.name, it.params) },
            fills = pluginManager.fills.get().values.map { EditorItemViewModel(it.id, it.name, it.params) },
        )

        return HttpResponse.ok(data)
    }
}
