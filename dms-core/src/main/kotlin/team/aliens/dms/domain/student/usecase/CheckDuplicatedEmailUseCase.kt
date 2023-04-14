package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.user.exception.UserEmailExistsException
import team.aliens.dms.domain.user.spi.QueryUserPort

@ReadOnlyUseCase
class CheckDuplicatedEmailUseCase(
    private val queryUserPort: QueryUserPort
) {

    fun execute(email: String) {
        if (queryUserPort.existsUserByEmail(email)) {
            throw UserEmailExistsException
        }
    }
}
