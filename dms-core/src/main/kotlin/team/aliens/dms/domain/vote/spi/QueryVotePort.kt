package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.vo.OptionVotingResultVO
import team.aliens.dms.domain.vote.spi.vo.StudentVotingResultVO
import java.util.UUID

interface QueryVotePort {
    fun queryVotingTopicById(votingTopicId: UUID): VotingTopic?
    fun queryVotingOptionById(votingOptionId: UUID): VotingOption?
    fun queryVoteById(voteId: UUID): Vote?
    fun queryAllVotingTopics(): List<VotingTopic>?
    fun queryVotingOptionsByVotingTopicId(votingTopicId: UUID): List<VotingOption>?
    fun queryStudentVotingByVotingTopicIdAndGrade(votingTopicId: UUID, grade: Int): List<StudentVotingResultVO>
    fun queryOptionVotingByVotingTopicId(votingTopicId: UUID): List<OptionVotingResultVO>
    fun existVotingTopicByName(votingTopicName: String): Boolean
    fun existVotingTopicById(votingTopicId: UUID): Boolean
    fun existVotingOptionById(votingOptionId: UUID): Boolean
    fun queryVoteByStudentId(studentId: UUID): List<Vote>
}
