package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.service.VoteService
import java.util.UUID
@UseCase
class RemoveVoteUseCase(
    private val voteService: VoteService
) {
    fun execute(voteId: UUID) {
        val vote: Vote = voteService.getVote(voteId)
        voteService.deleteVote(vote.id)
    }
}
