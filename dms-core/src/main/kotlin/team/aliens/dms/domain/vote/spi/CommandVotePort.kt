package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.model.VotingTopic
import java.util.UUID

interface CommandVotePort {
    fun saveVotingTopic(votingTopic: VotingTopic): VotingTopic
    fun saveVote(vote: Vote): Vote
    fun saveVotingOption(votingOption: VotingOption): VotingOption
    fun deleteVotingTopicByVotingTopicId(votingTopicId: UUID)
    fun deleteVotingOptionByVotingOptionId(votingOptionId: UUID)
    fun deleteVoteById(voteId: UUID)
}
