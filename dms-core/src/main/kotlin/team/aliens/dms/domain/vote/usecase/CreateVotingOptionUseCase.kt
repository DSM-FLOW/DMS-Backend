package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.vote.dto.request.CreateVotingOptionRequest
import team.aliens.dms.domain.vote.exception.VoteTypeMismatchException
import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.service.VoteService
import java.util.UUID

@UseCase
class CreateVotingOptionUseCase(
    private val voteService: VoteService
) {
    fun execute(request: CreateVotingOptionRequest): UUID {
        val votingTopic = voteService.getVotingTopic(request.votingTopicId)
        if (votingTopic!!.voteType != VoteType.OPTION_VOTE) {
            throw VoteTypeMismatchException
        }
        return voteService.createVotingOption(
            VotingOption(
                votingTopicId = votingTopic.id,
                optionName = request.name
            )
        ).id
    }
}
