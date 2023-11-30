package io.sparkled.udpserver.impl.command

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.sparkled.model.SequenceModel
import io.sparkled.model.SongModel
import io.sparkled.model.enumeration.SequenceStatus
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.model.setting.SettingsCacheEntry
import io.sparkled.model.util.testStageProp
import io.sparkled.music.SequencePlaybackState
import java.net.InetAddress
import java.nio.ByteBuffer

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
                settings = SettingsCacheEntry(0),
                playbackState = SequencePlaybackState(
                    sequences = listOf(sequence),
                    sequenceIndex = 0,
                    progressFunction = { 0.0 },
                    song = SongModel(name = "", durationMs = 0),
                    songAudio = ByteBuffer.allocate(0),
                    renderedStageProps = RenderedStagePropDataMap().apply {
                        this["P1"] = RenderedStagePropData(0, 0, 0, byteArrayOf())
                    },
                    stageProps = mapOf(
                        "1" to testStageProp.copy(id = "1", code = "P1", displayOrder = 1),
                        "2" to testStageProp.copy(id = "2", code = "P2", displayOrder = 3),
                        "3" to testStageProp.copy(id = "3", code = "P3", displayOrder = 2)
                    )
                )
            )

            val responseString = String(response)
            responseString shouldBe "P1:P3:P2"
        }
    }
}
