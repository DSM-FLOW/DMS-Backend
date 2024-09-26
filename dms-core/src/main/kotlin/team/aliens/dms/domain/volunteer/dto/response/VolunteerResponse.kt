package team.aliens.dms.domain.volunteer.dto.response

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.model.AvailableGrade
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.spi.vo.CurrentVolunteerApplicantVO
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerApplicantVO
import java.util.UUID

data class QueryMyVolunteerApplicationResponse(
    val volunteerApplications: List<VolunteerApplicationResponse>
) {
    companion object {
        fun of(
            applicationsWithVolunteers: List<Pair<VolunteerApplication, Volunteer>>
        ): QueryMyVolunteerApplicationResponse {
            return QueryMyVolunteerApplicationResponse(
                volunteerApplications = applicationsWithVolunteers.map { (application, volunteer) ->
                    VolunteerApplicationResponse.of(application, volunteer)
                }
            )
        }
    }
}

data class VolunteerApplicationResponse(
    val id: UUID,
    val volunteerId: UUID,
    val approved: Boolean,
    val name: String,
) {
    companion object {
        fun of(volunteerApplication: VolunteerApplication, volunteer: Volunteer): VolunteerApplicationResponse {
            return VolunteerApplicationResponse(
                id = volunteerApplication.id,
                volunteerId = volunteerApplication.volunteerId,
                approved = volunteerApplication.approved,
                name = volunteer.name
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
    val availableSex: Sex,
    val availableGrade: AvailableGrade
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
                availableSex = volunteer.availableSex,
                availableGrade = volunteer.availableGrade
            )
        }
    }
}

data class VolunteersResponse(
    val volunteers: List<VolunteerResponse>
)

data class VolunteerApplicantResponse(
    val id: UUID,
    val gcd: String,
    val name: String,
) {
    companion object {
        fun of(applicants: VolunteerApplicantVO) = VolunteerApplicantResponse(
            id = applicants.id,
            gcd = applicants.gcn,
            name = applicants.name
        )
    }
}

data class VolunteerApplicantsResponse(
    val applicants: List<VolunteerApplicantResponse>
)

data class CurrentVolunteerApplicantResponse(
    val volunteerName: String,
    val availableSex: Sex,
    val availableGrade: AvailableGrade,
    val applicants: List<VolunteerApplicantResponse>
) {
    companion object {
        fun of(currentVolunteerApplicant: CurrentVolunteerApplicantVO) = CurrentVolunteerApplicantResponse(
            volunteerName = currentVolunteerApplicant.volunteerName,
            availableSex = currentVolunteerApplicant.availableSex,
            availableGrade = currentVolunteerApplicant.availableGrade,
            applicants = currentVolunteerApplicant.applicants
                .map { VolunteerApplicantResponse.of(it) }
        )
    }
}

data class CurrentVolunteerApplicantsResponse(
    val volunteers: List<CurrentVolunteerApplicantResponse>
)

data class AvailableVolunteerResponse(
    val id: UUID,
    val name: String,
    val content: String,
    val score: Int,
    val optionalScore: Int,
    val maxApplicants: Int
) {
    companion object {
        fun of(volunteer: Volunteer): AvailableVolunteerResponse {
            return AvailableVolunteerResponse(
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

data class AvailableVolunteersResponse(
    val volunteers: List<AvailableVolunteerResponse>
)
