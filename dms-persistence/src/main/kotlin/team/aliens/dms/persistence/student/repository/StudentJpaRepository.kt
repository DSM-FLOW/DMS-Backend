package team.aliens.dms.persistence.student.repository

import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.student.entity.StudentJpaEntity

@Repository
interface StudentJpaRepository : CrudRepository<StudentJpaEntity, UUID> {

    fun findByUserId(userId: UUID): StudentJpaEntity

    fun findByRoomSchoolIdAndGradeAndClassRoomAndNumber(
        schoolId: UUID,
        grade: Int,
        classRoom: Int,
        number: Int
    ): StudentJpaEntity?

    fun existsByGradeAndClassRoomAndNumber(grade: Int, classRoom: Int, number: Int): Boolean

    fun existsByUserId(userId: UUID): Boolean

    fun findAllByIdIn(ids: List<UUID>): List<StudentJpaEntity>
}
