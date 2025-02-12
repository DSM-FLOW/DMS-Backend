package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.service.VoteService
import java.util.UUID
@UseCase
class RemoveVotingOptionUseCase(
    private val voteService: VoteService
) {
    fun execute(votingOptionId: UUID) {
        val votingOption: VotingOption = voteService.getVotingOption(votingOptionId)
        voteService.deleteVotingOption(votingOption.id)
    }
}
