package team.aliens.dms.domain.volunteer.spi

import team.aliens.dms.domain.volunteer.model.Volunteer
import java.util.UUID

interface QueryVolunteerPort {

    fun queryVolunteerById(volunteerId: UUID): Volunteer?

    fun queryVolunteerByCondition(studentId: UUID): List<Volunteer>

    fun queryAllVolunteersBySchoolId(schoolId: UUID): List<Volunteer>

    fun queryAllVolunteers(): List<Volunteer>
}
