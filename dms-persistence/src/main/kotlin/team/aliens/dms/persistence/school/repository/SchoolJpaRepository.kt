package team.aliens.dms.persistence.school.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.util.UUID

@Repository
interface SchoolJpaRepository : CrudRepository<SchoolJpaEntity, UUID> {

    fun findByCode(code: String): SchoolJpaEntity?

    @Query("select s.id from SchoolJpaEntity s where s.name = :name")
    fun findIdByName(name: String): UUID?
}
