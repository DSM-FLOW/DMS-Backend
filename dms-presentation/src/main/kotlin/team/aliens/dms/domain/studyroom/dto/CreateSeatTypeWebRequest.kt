package team.aliens.dms.domain.studyroom.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class CreateSeatTypeWebRequest(

    @field:NotBlank
    val name: String?,

    @field:Size(min = 7, max = 7, message = "크기가 7글자 이여야 합니다")
    @field:NotBlank
    val color: String?

)
