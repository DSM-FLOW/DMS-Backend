package team.aliens.dms.manager

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import team.aliens.dms.domain.manager.dto.ResetPasswordManagerRequest
import team.aliens.dms.domain.manager.usecase.FindManagerAccountIdUseCase
import team.aliens.dms.domain.manager.usecase.ResetPasswordManagerUseCase
import team.aliens.dms.manager.dto.request.ResetPasswordManagerWebRequest
import team.aliens.dms.manager.dto.response.FindManagerAccountIdResponse
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Validated
@RequestMapping("/managers")
@RestController
class ManagerWebAdapter(
    private val findManagerAccountIdUseCase: FindManagerAccountIdUseCase,
    private val resetPasswordManagerUseCase: ResetPasswordManagerUseCase
) {

    @GetMapping("/account-id/{school-id}")
    fun findAccountId(
        @PathVariable("school-id") schoolId: UUID,
        @RequestParam @NotBlank answer: String
    ): FindManagerAccountIdResponse {
        val result = findManagerAccountIdUseCase.execute(
            schoolId = schoolId,
            answer = answer
        )

        return FindManagerAccountIdResponse(result);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/password/initialization")
    fun resetPassword(@RequestBody @Valid webRequest: ResetPasswordManagerWebRequest) {
        val request = ResetPasswordManagerRequest(
            accountId = webRequest.accountId,
            email = webRequest.email,
            authCode = webRequest.authCode,
            newPassword = webRequest.newPassword
        )

        resetPasswordManagerUseCase.execute(request)
    }
}