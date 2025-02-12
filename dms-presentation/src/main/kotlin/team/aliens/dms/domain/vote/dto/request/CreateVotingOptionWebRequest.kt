package team.aliens.dms.domain.vote.dto.request

import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class CreateVotingOptionWebRequest(
    @field:NotBlank
    val votingTopicId: UUID,
    @field:NotBlank
    val optionName: String
)
