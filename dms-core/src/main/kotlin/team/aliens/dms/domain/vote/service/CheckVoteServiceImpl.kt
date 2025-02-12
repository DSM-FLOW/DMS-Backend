package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.spi.QueryVotePort
import java.util.UUID

@Service
class CheckVoteServiceImpl(
    private val queryVotePort: QueryVotePort
) : CheckVoteService {
    override fun checkVotingTopicExistByName(name: String): Boolean = queryVotePort.existVotingTopicByName(name)
    override fun checkVotingTopicExist(id: UUID): Boolean = queryVotePort.existVotingTopicById(id)
    override fun checkVotingOptionExist(id: UUID): Boolean = queryVotePort.existVotingOptionById(id)
    override fun checkVoteExistByStudentIdAndVotingTopicId(studentId: UUID, votingTopicId: UUID): Boolean {
        queryVotePort.queryVoteByStudentId(studentId).forEach { vote ->
            if (vote.votingTopicId == votingTopicId) {
                return true
            }
        }
        return false
    }
}
