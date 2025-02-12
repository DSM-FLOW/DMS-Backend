package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.exception.NotFoundVotingTopicException
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.QueryVotingTopicPort
import java.util.UUID

@Service
class GetVotingTopicServiceImpl(
    private val queryVotingTopicPort: QueryVotingTopicPort
) : GetVotingTopicService {

    override fun getVotingTopicById(id: UUID): VotingTopic {
        return queryVotingTopicPort.findById(id) ?: throw NotFoundVotingTopicException
    }

    override fun getAllVotingTopics(): List<VotingTopic?> {
        return queryVotingTopicPort.findAll()
    }
}
