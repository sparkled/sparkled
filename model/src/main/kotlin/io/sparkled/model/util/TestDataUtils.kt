package io.sparkled.model.util

import io.sparkled.model.PlaylistModel
import io.sparkled.model.PlaylistSequenceModel
import io.sparkled.model.ScheduledActionModel
import io.sparkled.model.SequenceChannelModel
import io.sparkled.model.SequenceModel
import io.sparkled.model.SettingModel
import io.sparkled.model.SongModel
import io.sparkled.model.StageModel
import io.sparkled.model.StagePropModel
import io.sparkled.model.enumeration.ScheduledActionType
import io.sparkled.model.enumeration.SequenceStatus
import io.sparkled.model.enumeration.StagePropType

/**
 * Model fields are mutable, so return a new value every time.
 */
val testPlaylist
    get() = PlaylistModel(
        id = "1",
        name = "Test playlist",
    )

val testPlaylistSequence
    get() = PlaylistSequenceModel(
        id = "1",
        playlistId = "1",
        sequenceId = "1",
        displayOrder = 1,
    )

val testScheduledAction
    get() = ScheduledActionModel(
        id = "1",
        playlistId = null,
        type = ScheduledActionType.SET_BRIGHTNESS,
        cronExpression = "0 * * * *",
        value = "100",
    )

val testSequenceChannel
    get() = SequenceChannelModel(
        id = "1",
        sequenceId = "1",
        stagePropId = "1",
        name = "Test sequence channel",
        displayOrder = 1,
    )

val testSequence
    get() = SequenceModel(
        id = "1",
        stageId = "1",
        songId = "1",
        status = SequenceStatus.PUBLISHED,
        name = "Test sequence",
        framesPerSecond = 30,
    )

val testSetting
    get() = SettingModel(
        id = "1",
        code = "TEST_SETTING",
        value = "Test setting value",
    )

val testSong
    get() = SongModel(
        id = "1",
        name = "Test song",
        artist = "Test song artist",
        durationMs = 30000,
    )

val testStage
    get() = StageModel(
        id = "1",
        name = "Test stage",
        width = 800,
        height = 600,
    )

val testStageProp
    get() = StagePropModel(
        id = "1",
        stageId = "1",
        code = "TEST_STAGE_PROP",
        name = "Test stage prop",
        type = StagePropType.LINE,
        displayOrder = 1,
        ledCount = 16,
        positionX = 100,
        positionY = 100,
        scaleX = 1.0,
        scaleY = 1.0,
        rotation = 0.0,
        brightness = StagePropModel.MAX_BRIGHTNESS,
    )
