package team.aliens.dms.persistence.vote.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.vote.entity.VotingTopicJpaEntity
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface VotingTopicJpaRepository : CrudRepository<VotingTopicJpaEntity, UUID> {

    fun findStartTimeById(id: UUID): LocalDateTime?

    fun findEndTimeById(id: UUID): LocalDateTime?
}
