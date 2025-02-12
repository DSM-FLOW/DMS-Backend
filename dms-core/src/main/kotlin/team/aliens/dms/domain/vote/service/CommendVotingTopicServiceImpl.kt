package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.CommendVotingTopicPort
import java.util.UUID


@Service
class CommendVotingTopicServiceImpl(
    val commendVotingTopicPort: CommendVotingTopicPort
) : CommendVotingTopicService {

    override fun saveVotingTopic(voteTopic: VotingTopic): UUID {
        return commendVotingTopicPort.saveVotingTopic(voteTopic)
    }

    override fun deleteVotingTopicById(id: UUID) {
        commendVotingTopicPort.deleteVotingTopicById(id)
    }

}
