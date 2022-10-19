package team.aliens.dms.auth.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CertifyEmailCodeWebRequest(

    @field:Email
    @field:NotBlank
    val email: String,

    @field:NotBlank
    val authCode: String,

    @field:NotNull
    val type: EmailType
)
