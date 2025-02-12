package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.vote.exception.AlreadyVotedException
import team.aliens.dms.domain.vote.exception.WrongVoteTypeException
import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.service.VoteService
import java.time.LocalDateTime
import java.util.UUID

@UseCase
class CreateVoteUseCase(
    private val voteService: VoteService,
    private val studentService: StudentService
) {
    fun execute(selectedId: UUID, votingTopicId: UUID) {
        val student = studentService.getCurrentStudent()
        if (voteService.checkVoteExistByStudentIdAndVotingTopicId(student.id, votingTopicId)) {
            throw AlreadyVotedException
        }
        val votingTopic: VotingTopic? = voteService.getVotingTopic(votingTopicId)

        when (votingTopic!!.voteType) {
            VoteType.OPTION_VOTE, VoteType.APPROVAL_VOTE -> {
                val selectedOption: VotingOption = voteService.getVotingOption(selectedId)
                voteService.createVote(
                    Vote(
                        studentId = student.id,
                        votingTopicId = votingTopic.id,
                        votedAt = LocalDateTime.now(),
                        selectedOptionId = selectedOption.id,
                        selectedStudentId = null
                    )
                )
            }
            VoteType.STUDENT_VOTE, VoteType.MODEL_STUDENT_VOTE -> {
                val selectedStudent = studentService.getStudentById(selectedId)
                voteService.createVote(
                    Vote(
                        studentId = student.id,
                        votingTopicId = votingTopic.id,
                        votedAt = LocalDateTime.now(),
                        selectedOptionId = null,
                        selectedStudentId = selectedStudent.id
                    )
                )
            }
            else -> throw WrongVoteTypeException
        }
    }
}
