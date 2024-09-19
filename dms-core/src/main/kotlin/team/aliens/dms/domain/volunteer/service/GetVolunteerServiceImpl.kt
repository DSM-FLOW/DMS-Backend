package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.volunteer.exception.VolunteerApplicationNotFoundException
import team.aliens.dms.domain.volunteer.exception.VolunteerNotFoundException
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.spi.QueryVolunteerApplicationPort
import team.aliens.dms.domain.volunteer.spi.QueryVolunteerPort
import team.aliens.dms.domain.volunteer.spi.vo.CurrentVolunteerApplicantVO
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerApplicantVO
import java.util.UUID

@Service
class GetVolunteerServiceImpl(
    private val queryVolunteerApplicationPort: QueryVolunteerApplicationPort,
    private val queryVolunteerPort: QueryVolunteerPort,
) : GetVolunteerService {

    override fun getVolunteerApplicationById(volunteerApplicationId: UUID): VolunteerApplication =
        queryVolunteerApplicationPort.queryVolunteerApplicationById(volunteerApplicationId)
            ?: throw VolunteerApplicationNotFoundException

    override fun getVolunteerById(volunteerId: UUID): Volunteer =
        queryVolunteerPort.queryVolunteerById(volunteerId)
            ?: throw VolunteerNotFoundException

    override fun getVolunteerByStudentId(studentId: UUID): List<Volunteer> =
        queryVolunteerPort.queryVolunteerByStudentId(studentId)

    override fun getVolunteerApplicationsByStudentId(studentId: UUID): List<VolunteerApplication> =
        queryVolunteerApplicationPort.queryVolunteerApplicationsByStudentId(studentId)

    override fun getAllVolunteersBySchoolId(schoolId: UUID): List<Volunteer> =
        queryVolunteerPort.queryAllVolunteersBySchoolId(schoolId)

    override fun getAllApplicantsByVolunteerId(volunteerId: UUID): List<VolunteerApplicantVO> =
        queryVolunteerApplicationPort.queryAllApplicantsByVolunteerId(volunteerId)

    override fun getAllApplicantsBySchoolIdGroupByVolunteer(schoolId: UUID): List<CurrentVolunteerApplicantVO> =
        queryVolunteerApplicationPort.queryAllApplicantsBySchoolIdGroupByVolunteer(schoolId)

    override fun getAllVolunteers(): List<Volunteer> =
        queryVolunteerPort.queryAllVolunteers()

    override fun getVolunteerApplicationsWithVolunteersByStudentId(studentId: UUID): List<Pair<VolunteerApplication, Volunteer>> {
        return queryVolunteerApplicationPort.getVolunteerApplicationsWithVolunteersByStudentId(studentId)
    }
}
