package team.aliens.dms.persistence.remain.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.remain.entity.RemainOptionJpaEntity
import java.util.UUID


@Repository
interface RemainRecordJpaRepository : CrudRepository<RemainOptionJpaEntity, UUID> {
}