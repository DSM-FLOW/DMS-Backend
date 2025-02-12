package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.exception.VotingTopicAlreadyExistException
import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.CommandVotePort
import team.aliens.dms.domain.vote.spi.QueryVotePort
import java.util.UUID

@Service
class CommandVoteServiceImpl(
    private val commandVotePort: CommandVotePort,
    private val queryVotePort: QueryVotePort
) : CommandVoteService {
    override fun createVotingTopic(votingTopic: VotingTopic): VotingTopic {
        if (checkVotingTopic(votingTopic.topicName)) {
            throw VotingTopicAlreadyExistException
        }
        return commandVotePort.saveVotingTopic(votingTopic)
    }

    override fun createVote(vote: Vote): Vote = commandVotePort.saveVote(vote)
    override fun createVotingOption(votingOption: VotingOption): VotingOption {
        return commandVotePort.saveVotingOption(votingOption)
    }

    override fun deleteVotingTopic(votingTopicId: UUID) = commandVotePort.deleteVotingTopicByVotingTopicId(votingTopicId)
    override fun deleteVotingOption(
        votingOptionId: UUID
    ) = commandVotePort.deleteVotingOptionByVotingOptionId(votingOptionId)
    override fun deleteVote(voteId: UUID) = commandVotePort.deleteVoteById(voteId)
    override fun checkVotingTopic(name: String): Boolean = queryVotePort.existVotingTopicByName(name)
}
