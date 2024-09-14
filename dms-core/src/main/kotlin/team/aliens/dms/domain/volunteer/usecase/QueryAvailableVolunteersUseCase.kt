package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.volunteer.dto.response.AvailableVolunteerResponse
import team.aliens.dms.domain.volunteer.dto.response.VolunteerResponse
import team.aliens.dms.domain.volunteer.service.VolunteerService

@ReadOnlyUseCase
class QueryAvailableVolunteersUseCase(
    private val volunteerService: VolunteerService,
    private val studentService: StudentService
) {

    fun execute(): List<AvailableVolunteerResponse> {
        val student = studentService.getCurrentStudent()

        val availableVolunteers = volunteerService.getVolunteerByCondition(student.id)

        return availableVolunteers.map { volunteer ->
            AvailableVolunteerResponse(
                id = volunteer.id,
                name = volunteer.name,
                content = volunteer.content,
                score = volunteer.score,
                optionalScore = volunteer.optionalScore,
                maxApplicants = volunteer.maxApplicants
            )
        }
    }
}
