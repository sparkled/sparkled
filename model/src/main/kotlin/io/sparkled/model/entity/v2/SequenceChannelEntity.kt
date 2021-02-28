package io.sparkled.model.entity.v2

import io.sparkled.model.annotation.Entity
import io.sparkled.model.util.IdUtils
import java.util.UUID

@Entity("sequence_channel", idField = "uuid")
data class SequenceChannelEntity(
    val uuid: UUID = IdUtils.newUuid(),
    val sequenceId: Int,
    val stagePropUuid: UUID,
    val name: String,
    val displayOrder: Int,
    val channelJson: String
) : SparkledEntity
