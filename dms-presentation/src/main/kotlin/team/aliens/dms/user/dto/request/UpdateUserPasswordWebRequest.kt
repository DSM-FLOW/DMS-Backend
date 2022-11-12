package team.aliens.dms.user.dto.request

import com.fasterxml.jackson.annotation.JsonFormat
import team.aliens.dms.manager.dto.request.Password
import javax.validation.constraints.NotBlank

data class UpdateUserPasswordWebRequest(

    @field:JsonFormat(shape = JsonFormat.Shape.STRING)
    val oldPassword: Password,

    @field:NotBlank
    @field:JsonFormat(shape = JsonFormat.Shape.STRING)
    val newPassword: Password

)
