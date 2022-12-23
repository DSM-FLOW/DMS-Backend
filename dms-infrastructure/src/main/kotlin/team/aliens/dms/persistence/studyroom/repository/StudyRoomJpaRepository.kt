package team.aliens.dms.persistence.studyroom.repository

import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.studyroom.entity.StudyRoomJpaEntity

@Repository
interface StudyRoomJpaRepository : CrudRepository<StudyRoomJpaEntity, UUID> {

    fun existsByNameAndFloor(name: String, floor: Int): Boolean

}