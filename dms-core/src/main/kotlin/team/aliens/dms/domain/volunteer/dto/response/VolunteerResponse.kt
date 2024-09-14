package team.aliens.dms.domain.volunteer.dto.response

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.model.GradeCondition
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import java.util.UUID

data class QueryMyVolunteerApplicationResponse(
    val volunteerApplications: List<VolunteerApplicationResponse>
) {
    companion object {
        fun of(volunteerApplications: List<VolunteerApplication>): QueryMyVolunteerApplicationResponse {
            return QueryMyVolunteerApplicationResponse(
                volunteerApplications = volunteerApplications.map { VolunteerApplicationResponse.of(it) }
            )
        }
    }
}

data class VolunteerApplicationResponse(
    val id: UUID,
    val volunteerId: UUID,
    val approved: Boolean
) {
    companion object {
        fun of(volunteerApplication: VolunteerApplication): VolunteerApplicationResponse {
            return VolunteerApplicationResponse(
                id = volunteerApplication.id,
                volunteerId = volunteerApplication.volunteerId,
                approved = volunteerApplication.approved
            )
        }
    }
}

data class VolunteerResponse(
    val id: UUID,
    val name: String,
    val content: String,
    val score: Int,
    val optionalScore: Int,
    val maxApplicants: Int,
    val sexCondition: Sex,
    val gradeCondition: GradeCondition
) {
    companion object {
        fun of(volunteer: Volunteer): VolunteerResponse {
            return VolunteerResponse(
                id = volunteer.id,
                name = volunteer.name,
                content = volunteer.content,
                score = volunteer.score,
                optionalScore = volunteer.optionalScore,
                maxApplicants = volunteer.maxApplicants,
                sexCondition = volunteer.sexCondition,
                gradeCondition = volunteer.gradeCondition
            )
        }
    }
}

data class VolunteersResponse(
    val volunteers: List<VolunteerResponse>
)
