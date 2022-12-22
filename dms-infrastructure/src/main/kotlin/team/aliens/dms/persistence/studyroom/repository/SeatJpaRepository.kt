package team.aliens.dms.persistence.studyroom.repository

import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.SeatJpaEntity

@Repository
interface SeatJpaRepository : CrudRepository<SeatJpaEntity, UUID> {

    fun findByStudentId(studentId: UUID): SeatJpaEntity?

    fun countByStudyRoomId(studyRoomId: UUID): Int

    fun findAllByStudyRoomId(studyRoomId: UUID): List<SeatJpaEntity>

}