package team.aliens.dms.domain.vote.model

import java.util.UUID

class VotingOption(
    val id: UUID = UUID(0, 0),
    val votingTopicId: UUID = UUID(0, 0),
    val optionName: String
)
