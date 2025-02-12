package team.aliens.dms.domain.vote.dto.reponse

import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.domain.vote.model.VotingTopic
import java.time.LocalDateTime
import java.util.UUID


data class GetVotingTopicResponse(
    val id: UUID,
    val topicName: String,
    val description: String,
    val voteType: VoteType,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
) {
    companion object {
        fun from(votingTopic: VotingTopic): GetVotingTopicResponse {
            return GetVotingTopicResponse(
                votingTopic.id,
                votingTopic.topicName,
                votingTopic.description,
                votingTopic.voteType,
                votingTopic.startTime,
                votingTopic.endTime
            )
        }
    }
}
