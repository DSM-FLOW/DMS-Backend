package team.aliens.dms.domain.vote.service

import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.vo.OptionVotingResultVO
import team.aliens.dms.domain.vote.spi.vo.StudentVotingResultVO
import java.util.UUID

interface GetVoteService {
    fun getVotingTopic(votingTopicId: UUID): VotingTopic?
    fun getAllVotingTopics(): List<VotingTopic>?
    fun getVotingOptionsByVotingTopicId(votingTopicId: UUID): List<VotingOption>?
    fun getVotesInStudentVoting(votingTopicId: UUID, grade: Int): List<StudentVotingResultVO>
    fun getVotesInOptionVoting(votingTopicId: UUID): List<OptionVotingResultVO>
    fun getVotingOption(votingOptionId: UUID): VotingOption
    fun getVote(voteId: UUID): Vote
}
