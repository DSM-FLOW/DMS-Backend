package team.aliens.dms.domain.vote.service

import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.model.VotingTopic
import java.util.UUID

interface CommandVoteService {
    fun createVotingTopic(votingTopic: VotingTopic): VotingTopic
    fun createVote(vote: Vote): Vote
    fun createVotingOption(votingOption: VotingOption): VotingOption
    fun deleteVotingTopic(votingTopicId: UUID)
    fun deleteVotingOption(votingOptionId: UUID)
    fun deleteVote(voteId: UUID)
    fun checkVotingTopic(name: String): Boolean
}
