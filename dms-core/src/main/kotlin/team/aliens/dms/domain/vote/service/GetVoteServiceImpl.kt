package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.exception.VoteNotFoundException
import team.aliens.dms.domain.vote.exception.VotingOptionNotFoundException
import team.aliens.dms.domain.vote.exception.VotingTopicNotFoundException
import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.QueryVotePort
import team.aliens.dms.domain.vote.spi.vo.OptionVotingResultVO
import team.aliens.dms.domain.vote.spi.vo.StudentVotingResultVO
import java.util.*

@Service
class GetVoteServiceImpl(
    private val queryVotePort: QueryVotePort
) : GetVoteService {
    override fun getVotingTopic(
        votingTopicId: UUID
    ): VotingTopic = queryVotePort.queryVotingTopicById(votingTopicId) ?: throw VotingTopicNotFoundException
    override fun getAllVotingTopics(): List<VotingTopic>? = queryVotePort.queryAllVotingTopics()

    override fun getVotingOptionsByVotingTopicId(votingTopicId: UUID): List<VotingOption>? = queryVotePort.queryVotingOptionsByVotingTopicId(votingTopicId)

    override fun getVotesInStudentVoting(votingTopicId: UUID, grade: Int): List<StudentVotingResultVO> {
        return queryVotePort.queryStudentVotingByVotingTopicIdAndGrade(votingTopicId, grade)
    }

    override fun getVotesInOptionVoting(votingTopicId: UUID): List<OptionVotingResultVO> {
        return queryVotePort.queryOptionVotingByVotingTopicId(votingTopicId)
    }

    override fun getVotingOption(votingOptionId: UUID): VotingOption {
        return queryVotePort.queryVotingOptionById(votingOptionId) ?: throw VotingOptionNotFoundException
    }

    override fun getVote(voteId: UUID): Vote {
        return queryVotePort.queryVoteById(voteId) ?: throw VoteNotFoundException
    }
}
