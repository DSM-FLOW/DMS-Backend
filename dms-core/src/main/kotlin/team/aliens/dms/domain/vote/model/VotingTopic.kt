package team.aliens.dms.domain.vote.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class VotingTopic(
    val id: UUID = UUID(0, 0),

    val topicName: String,

    val description: String,

    val startTime: LocalDateTime,

    val endTime: LocalDateTime,

    val voteType: VoteType,

    val managerId: UUID,
)
