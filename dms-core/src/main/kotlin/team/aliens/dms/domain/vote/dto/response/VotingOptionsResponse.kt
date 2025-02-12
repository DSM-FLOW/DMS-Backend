package team.aliens.dms.domain.vote.dto.response

import java.util.UUID

class VotingOptionsResponse(
    val id: UUID,
    val votingOptionName: String,
) {
    companion object {
        fun of(
            id: UUID,
            votingOptionName: String
        ) = VotingOptionsResponse(
            id = id,
            votingOptionName = votingOptionName
        )
    }
}
