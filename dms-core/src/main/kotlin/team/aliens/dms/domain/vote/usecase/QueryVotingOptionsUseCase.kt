package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.vote.dto.response.VotingOptionsResponse
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.service.VoteService
import java.util.UUID

@UseCase
class QueryVotingOptionsUseCase(
    private val votingService: VoteService
) {
    fun execute(votingTopicId: UUID): List<VotingOptionsResponse> {
        val votingTopic: VotingTopic = votingService.getVotingTopic(votingTopicId)!!

        return votingService.getVotingOptionsByVotingTopicId(votingTopic.id)
            ?.map { votingOption ->
                VotingOptionsResponse(
                    id = votingOption.id,
                    votingOptionName = votingOption.optionName
                )
            } ?: emptyList()
    }
}
