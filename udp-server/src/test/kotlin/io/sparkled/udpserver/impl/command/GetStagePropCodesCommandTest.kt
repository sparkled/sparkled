package io.sparkled.udpserver.impl.command

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.sparkled.model.SequenceModel
import io.sparkled.model.SongModel
import io.sparkled.model.StagePropModel
import io.sparkled.model.enumeration.SequenceStatus
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.model.setting.SettingsCache
import io.sparkled.music.PlaybackState
import java.net.InetAddress

class GetStagePropCodesCommandTest : StringSpec() {

    init {
        "can retrieve stage prop codes" {
            val command = GetStagePropCodesCommand()
            val sequence = SequenceModel(
                songId = "0",
                stageId = "0",
                framesPerSecond = 0,
                name = "",
                status = SequenceStatus.NEW,
            )

            val response = command.handle(
                ipAddress = InetAddress.getLocalHost(),
                port = 2812,
                args = listOf(GetStagePropCodesCommand.KEY),
                settings = SettingsCache(0),
                playbackState = PlaybackState(
                    sequences = listOf(sequence),
                    sequenceIndex = 0,
                    progressFunction = { 0.0 },
                    sequence = sequence,
                    song = SongModel(name = "", durationMs = 0),
                    songAudio = byteArrayOf(1),
                    renderedStageProps = RenderedStagePropDataMap().apply {
                        this["P1"] = RenderedStagePropData(0, 0, 0, byteArrayOf())
                    },
                    stageProps = mapOf(
                        "P1" to StagePropModel(code = "P1", displayOrder = 1),
                        "P2" to StagePropModel(code = "P2", displayOrder = 3),
                        "P3" to StagePropModel(code = "P3", displayOrder = 2)
                    )
                )
            )

            val responseString = String(response)
            responseString shouldBe "P1:P3:P2"
        }
    }
}
