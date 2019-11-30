package io.sparkled.udpserver.impl.command

import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.Song
import io.sparkled.model.entity.SongAudio
import io.sparkled.model.entity.StageProp
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.model.setting.SettingsCache
import io.sparkled.music.PlaybackState
import java.nio.charset.StandardCharsets
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test
import java.net.InetAddress

internal class GetStagePropCodesCommandTest {

    @Test
    fun can_retrieve_stage_prop_codes() {
        val command = GetStagePropCodesCommand()
        val response = command.handle(
            ipAddress = InetAddress.getLocalHost(),
            port = 2812,
            args = listOf(GetStagePropCodesCommand.KEY),
            settings = SettingsCache(0),
            playbackState = PlaybackState(
                sequences = emptyList(),
                sequenceIndex = 0,
                progressFunction = { 0.0 },
                sequence = Sequence(),
                song = Song(),
                songAudio = SongAudio(),
                renderedStageProps = RenderedStagePropDataMap(),
                stageProps = mapOf(
                    "P1" to StageProp().setCode("P1").setDisplayOrder(1),
                    "P2" to StageProp().setCode("P2").setDisplayOrder(3),
                    "P3" to StageProp().setCode("P3").setDisplayOrder(2)
                )
            )
        )

        val responseString = String(response, StandardCharsets.UTF_8)
        assertThat(responseString, `is`("P1:P3:P2"))
    }
}
