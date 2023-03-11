package team.aliens.dms.persistence.studyroom.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.studyroom.entity.StudyRoomJpaEntity
import team.aliens.dms.persistence.studyroom.repository.StudyRoomTimeSlotJpaRepository

@Component
class StudyRoomMapper(
    private val schoolRepository: SchoolJpaRepository,
    private val studyRoomTimeSlotRepository: StudyRoomTimeSlotJpaRepository
) : GenericMapper<StudyRoom, StudyRoomJpaEntity> {

    override fun toDomain(entity: StudyRoomJpaEntity?): StudyRoom? {
        return entity?.let {
            StudyRoom(
                id = it.id!!,
                schoolId = entity.school!!.id!!,
                timeSlotId = entity.timeSlot!!.id!!,
                name = entity.name,
                floor = entity.floor,
                widthSize = entity.widthSize,
                heightSize = entity.heightSize,
                inUseHeadcount = entity.inUseHeadcount,
                availableHeadcount = entity.availableHeadcount,
                availableSex = entity.availableSex,
                availableGrade = entity.availableGrade,
                eastDescription = entity.eastDescription,
                westDescription = entity.westDescription,
                southDescription = entity.southDescription,
                northDescription = entity.northDescription
            )
        }
    }

    override fun toEntity(domain: StudyRoom): StudyRoomJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)
        val timeSlot = domain.timeSlotId?.let { studyRoomTimeSlotRepository.findByIdOrNull(it) }

        return StudyRoomJpaEntity(
            id = domain.id,
            school = school,
            timeSlot = timeSlot,
            name = domain.name,
            floor = domain.floor,
            widthSize = domain.widthSize,
            heightSize = domain.heightSize,
            inUseHeadcount = domain.inUseHeadcount!!,
            availableHeadcount = domain.availableHeadcount,
            availableSex = domain.availableSex,
            availableGrade = domain.availableGrade,
            eastDescription = domain.eastDescription,
            westDescription = domain.westDescription,
            southDescription = domain.southDescription,
            northDescription = domain.northDescription
        )
    }
}
