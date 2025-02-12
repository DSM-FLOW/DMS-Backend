package team.aliens.dms.domain.vote.dto.request

import team.aliens.dms.domain.vote.model.VoteType
import java.time.LocalDateTime
import java.util.UUID


data class UpdateVotingTopicRequest(
    val id: UUID,
    val topicName: String,
    val description: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val voteType: VoteType
)
