package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.VotingTopic
import java.util.UUID

interface CommendVotingTopicPort {

    fun saveVotingTopic(votingTopic: VotingTopic): UUID

    fun deleteVotingTopicById(id: UUID)
}
