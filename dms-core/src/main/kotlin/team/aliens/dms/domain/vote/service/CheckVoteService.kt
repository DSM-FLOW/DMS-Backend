package team.aliens.dms.domain.vote.service

import java.util.UUID

interface CheckVoteService {
    fun checkVotingTopicExistByName(name: String): Boolean
    fun checkVotingTopicExist(id: UUID): Boolean
    fun checkVotingOptionExist(id: UUID): Boolean
    fun checkVoteExistByStudentIdAndVotingTopicId(studentId: UUID, votingTopicId: UUID): Boolean
}
