package team.aliens.dms.domain.vote.model

import java.time.LocalDateTime
import java.util.UUID

class VotingTopic(
    val id: UUID = UUID(0, 0),
    val topicName: String,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val voteType: VoteType,
    val managerId: UUID = UUID(0, 0)
)
