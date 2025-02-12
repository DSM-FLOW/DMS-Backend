package team.aliens.dms.persistence.vote.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.vote.entity.VoteJpaEntity
import java.util.UUID

@Repository
interface VoteJpaRepository : CrudRepository<VoteJpaEntity, UUID> {
    fun findByStudentId(studentId: UUID): List<VoteJpaEntity>
}
