package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.vote.dto.response.OptionVotingResponse
import team.aliens.dms.domain.vote.dto.response.StudentVotingResponse
import team.aliens.dms.domain.vote.exception.WrongVoteTypeException
import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.domain.vote.service.VoteService
import team.aliens.dms.domain.vote.spi.vo.OptionVotingResultVO
import team.aliens.dms.domain.vote.spi.vo.StudentVotingResultVO
import java.util.UUID

@UseCase
class QueryVotesUseCase(
    private val voteService: VoteService,
    private val studentService: StudentService
) {
    fun execute(votingTopicId: UUID): Any {
        val votingTopic = voteService.getVotingTopic(votingTopicId)

        return when (votingTopic?.voteType) {
            VoteType.STUDENT_VOTE, VoteType.MODEL_STUDENT_VOTE -> queryStudentVotingVotes(votingTopicId)
            VoteType.OPTION_VOTE, VoteType.APPROVAL_VOTE -> queryOptionVotingVotes(votingTopicId)

            else -> throw WrongVoteTypeException
        }
    }

    private fun queryOptionVotingVotes(votingTopicId: UUID): List<OptionVotingResponse> {
        val result: List<OptionVotingResultVO> = voteService.getVotesInOptionVoting(votingTopicId)
        println("Option voting result: $result") // 디버그 로그 추가
        return result.map { votingResult ->
            OptionVotingResponse(
                name = votingResult.name,
                votes = votingResult.votes
            )
        }
    }

    private fun queryStudentVotingVotes(votingTopicId: UUID): List<List<StudentVotingResponse>> {
        return listOf(1, 2, 3).map { grade ->
            val result: List<StudentVotingResultVO> = voteService.getVotesInStudentVoting(votingTopicId, grade)
            println("Student voting result for grade $grade: $result") // 디버그 로그 추가
            result.map { votingResult ->
                val student = studentService.getStudentById(votingResult.id)
                val studentNumber = String.format("%02d", student.number)
                val classNumber = "${student.grade}${student.classRoom}$studentNumber"
                StudentVotingResponse.of(
                    id = votingResult.id,
                    name = votingResult.name,
                    votes = votingResult.votes,
                    classNumber = classNumber
                )
            }
        }
    }
}
