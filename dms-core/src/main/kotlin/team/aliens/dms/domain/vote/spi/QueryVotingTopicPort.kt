package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.VotingTopic
import java.time.LocalDateTime
import java.util.UUID


interface QueryVotingTopicPort {

    fun findStartTimeById(id: UUID): LocalDateTime?

    fun findEndTimeById(id: UUID): LocalDateTime?

    fun findById(id: UUID): VotingTopic?

    fun findAll(): List<VotingTopic?>
}
