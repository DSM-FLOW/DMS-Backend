package team.aliens.dms.domain.vote.dto.request

import team.aliens.dms.domain.vote.model.VoteType
import java.time.LocalDateTime

data class CreateVoteTopicRequest(
    val topicName: String,
    val description: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val voteType: VoteType
)
