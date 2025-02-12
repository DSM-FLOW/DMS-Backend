package team.aliens.dms.domain.vote.dto.request

import java.util.*

class CreateVotingOptionRequest(
    val votingTopicId: UUID,
    val name: String
) {
    companion object {
        fun of(
            votingOptionId: UUID,
            name: String,
        ) = CreateVotingOptionRequest(
            votingTopicId = votingOptionId,
            name = name
        )
    }
}
