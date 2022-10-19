package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.domain.auth.dto.CertifyEmailRequest
import team.aliens.dms.domain.auth.exception.EmailMisMatchException
import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.global.annotation.ReadOnlyUseCase

@ReadOnlyUseCase
class CertifyEmailUseCase(
    private val queryUserPort: AuthQueryUserPort
) {

    fun execute(request: CertifyEmailRequest) {
        val user = queryUserPort.queryUserByAccountId(request.accountId)
            ?: throw UserNotFoundException;

        if (user.email != request.email) {
            throw EmailMisMatchException
        }
    }
}