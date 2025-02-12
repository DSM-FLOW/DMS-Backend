package team.aliens.dms.domain.vote.service

import team.aliens.dms.domain.vote.model.VotingTopic
import java.util.UUID

interface CommendVotingTopicService {

    fun saveVotingTopic(voteTopic: VotingTopic): UUID

    fun deleteVotingTopicById(id: UUID)
}
