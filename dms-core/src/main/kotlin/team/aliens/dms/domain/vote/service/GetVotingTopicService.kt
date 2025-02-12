package team.aliens.dms.domain.vote.service

import team.aliens.dms.domain.vote.model.VotingTopic
import java.util.UUID

interface GetVotingTopicService {

    fun getVotingTopicById(id: UUID): VotingTopic

    fun getAllVotingTopics(): List<VotingTopic?>
}
