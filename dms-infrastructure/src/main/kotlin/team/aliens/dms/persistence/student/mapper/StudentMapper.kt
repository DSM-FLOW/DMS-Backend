package team.aliens.dms.persistence.student.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.room.exception.RoomNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.room.entity.RoomJpaEntityId
import team.aliens.dms.persistence.room.repository.RoomJpaRepository
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import team.aliens.dms.persistence.user.repository.UserJpaRepository

@Component
class StudentMapper(
    private val roomJpaRepository: RoomJpaRepository,
    private val userJpaRepository: UserJpaRepository
) : GenericMapper<Student, StudentJpaEntity> {

    override fun toDomain(entity: StudentJpaEntity?): Student? {
        val room = entity?.roomJpaEntity?.let {
            roomJpaRepository.findByIdOrNull(it.id)
        } ?: throw RoomNotFoundException

        return Student(
            studentId = entity.studentId,
            roomNumber = room.id.roomNumber,
            schoolId = room.id.schoolId,
            grade = entity.grade,
            classRoom = entity.classRoom,
            number = entity.number
        )
    }

    override fun toEntity(domain: Student): StudentJpaEntity {
        val user = userJpaRepository.findByIdOrNull(domain.studentId) ?: throw UserNotFoundException

        val room =  roomJpaRepository.findByIdOrNull(
            RoomJpaEntityId(domain.roomNumber, domain.studentId)
        ) ?: throw RoomNotFoundException

        return StudentJpaEntity(
            studentId = domain.studentId,
            userJpaEntity = user,
            roomJpaEntity = room,
            grade = domain.grade,
            classRoom = domain.classRoom,
            number = domain.number
        )
    }
}