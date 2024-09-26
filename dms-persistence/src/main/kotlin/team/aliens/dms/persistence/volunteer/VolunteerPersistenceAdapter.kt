package team.aliens.dms.persistence.volunteer

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.spi.VolunteerPort
import team.aliens.dms.persistence.student.repository.StudentJpaRepository
import team.aliens.dms.persistence.volunteer.mapper.VolunteerMapper
import team.aliens.dms.persistence.volunteer.repository.VolunteerJpaRepository
import java.util.UUID

@Component
class VolunteerPersistenceAdapter(
    private val volunteerMapper: VolunteerMapper,
    private val volunteerJpaRepository: VolunteerJpaRepository
) : VolunteerPort {

    override fun saveVolunteer(volunteer: Volunteer): Volunteer = volunteerMapper.toDomain(
        volunteerJpaRepository.save(
            volunteerMapper.toEntity(volunteer)
        )
    )!!

    override fun deleteVolunteer(volunteer: Volunteer) {
        volunteerJpaRepository.delete(
            volunteerMapper.toEntity(volunteer)
        )
    }

    override fun queryVolunteerById(volunteerId: UUID): Volunteer? = volunteerMapper.toDomain(
        volunteerJpaRepository.findByIdOrNull(volunteerId)
    )

    override fun queryAllVolunteersBySchoolId(schoolId: UUID): List<Volunteer> {
        return volunteerJpaRepository.findAllBySchoolId(schoolId)
            .map { volunteerMapper.toDomain(it)!! }
    }
}
