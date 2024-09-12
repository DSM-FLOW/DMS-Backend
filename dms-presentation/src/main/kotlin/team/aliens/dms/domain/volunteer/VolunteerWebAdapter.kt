package team.aliens.dms.domain.volunteer

import jakarta.validation.Valid
import org.jetbrains.annotations.NotNull
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import team.aliens.dms.domain.volunteer.dto.request.CreateVolunteerRequest
import team.aliens.dms.domain.volunteer.dto.request.CreateVolunteerWebRequest
import team.aliens.dms.domain.volunteer.dto.request.UpdateVolunteerRequest
import team.aliens.dms.domain.volunteer.dto.request.UpdateVolunteerWebRequest
import team.aliens.dms.domain.volunteer.usecase.ApplyVolunteerUseCase
import team.aliens.dms.domain.volunteer.usecase.UnapplyVolunteerUseCase
import team.aliens.dms.domain.volunteer.usecase.CreateVolunteerUseCase
import team.aliens.dms.domain.volunteer.usecase.UpdateVolunteerUseCase
import java.util.UUID

@Validated
@RequestMapping("/volunteers")
@RestController
class VolunteerWebAdapter(
    private val applyVolunteerUseCase: ApplyVolunteerUseCase,
    private val unapplyVolunteerUseCase: UnapplyVolunteerUseCase,
    private val createVolunteerUseCase: CreateVolunteerUseCase,
    private val updateVolunteerUseCase: UpdateVolunteerUseCase,
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{volunteer-application-id}")
    fun applyVolunteer(@PathVariable("volunteer-application-id") @NotNull volunteerApplicationId: UUID) {
        applyVolunteerUseCase.execute(volunteerApplicationId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{volunteer-application-id}")
    fun unapplyVolunteer(@PathVariable("volunteer-application-id") @NotNull volunteerApplicationId: UUID) {
        unapplyVolunteerUseCase.execute(volunteerApplicationId)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createVolunteer(@Valid @RequestBody createVolunteerWebRequest: CreateVolunteerWebRequest) {
        createVolunteerUseCase.execute(
            CreateVolunteerRequest(
                name = createVolunteerWebRequest.name,
                content = createVolunteerWebRequest.content,
                gradeCondition = createVolunteerWebRequest.gradeCondition,
                sexCondition = createVolunteerWebRequest.sexCondition,
                score = createVolunteerWebRequest.score,
                optionalScore = createVolunteerWebRequest.optionalScore,
                maxApplicants = createVolunteerWebRequest.maxApplicants,
            )
        )
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{volunteer-id}")
    fun update(@Valid @RequestBody updateVolunteerWebRequest: UpdateVolunteerWebRequest,
               @PathVariable("volunteer-id") @NotNull volunteerApplicationId: UUID) {
        updateVolunteerUseCase.execute(
            UpdateVolunteerRequest(
                name = updateVolunteerWebRequest.name,
                content = updateVolunteerWebRequest.content,
                gradeCondition = updateVolunteerWebRequest.gradeCondition,
                sexCondition = updateVolunteerWebRequest.sexCondition,
                score = updateVolunteerWebRequest.score,
                optionalScore = updateVolunteerWebRequest.optionalScore,
                maxApplicants = updateVolunteerWebRequest.maxApplicants,
                volunteerId = volunteerApplicationId
            )
        )
    }
}
