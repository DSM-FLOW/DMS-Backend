package team.aliens.dms.persistence.vote.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.vote.entity.VotingOptionJpaEntity
import java.util.UUID

@Repository
interface VotingOptionJpaRepository : CrudRepository<VotingOptionJpaEntity, UUID>
