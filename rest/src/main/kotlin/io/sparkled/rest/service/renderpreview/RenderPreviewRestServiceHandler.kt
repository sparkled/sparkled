package io.sparkled.rest.service.renderpreview

import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.model.util.SequenceUtils
import io.sparkled.model.validator.SequenceChannelValidator
import io.sparkled.persistence.sequence.SequencePersistenceService
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.persistence.stage.StagePersistenceService
import io.sparkled.renderer.Renderer
import io.sparkled.rest.service.RestServiceHandler
import javax.inject.Inject
import javax.ws.rs.core.Response

open class RenderPreviewRestServiceHandler @Inject
constructor(
    private val sequencePersistenceService: SequencePersistenceService,
    private val songPersistenceService: SongPersistenceService,
    private val stagePersistenceService: StagePersistenceService
) : RestServiceHandler() {

    internal fun getRenderedSequence(startFrame: Int, frameCount: Int, sequenceChannels: List<SequenceChannel>?): Response {
        if (sequenceChannels == null || sequenceChannels.isEmpty()) {
            return respond(Response.Status.BAD_REQUEST, "Nothing to render.")
        }

        val sequenceOptional = sequencePersistenceService.getSequenceById(sequenceChannels[0].getSequenceId()!!)
        val songOptional = songPersistenceService.getSongBySequenceId(sequenceChannels[0].getSequenceId()!!)

        if (!sequenceOptional.isPresent) {
            return respond(Response.Status.NOT_FOUND, "Sequence not found.")
        } else if (!songOptional.isPresent) {
            return respond(Response.Status.NOT_FOUND, "Song not found.")
        }

        val sequence = sequenceOptional.get()
        val song = songOptional.get()

        val sequenceFrameCount = SequenceUtils.getFrameCount(song, sequence)

        // Ensure the duration doesn't go past the end of the last available frame for the sequence.
        val endFrame = Math.min(sequenceFrameCount, startFrame + frameCount) - 1
        val renderResult = getRenderResult(sequence, startFrame, endFrame, sequenceChannels)

        return respondOk(renderResult)
    }

    private fun getRenderResult(sequence: Sequence, startFrame: Int, endFrame: Int, sequenceChannels: List<SequenceChannel>): RenderedStagePropDataMap {
        val validator = SequenceChannelValidator()
        sequenceChannels.forEach(validator::validate)

        val stageProps = stagePersistenceService.getStagePropsByStageId(sequence.getStageId()!!)
        return Renderer(sequence, sequenceChannels, stageProps, startFrame, endFrame).render()
    }
}