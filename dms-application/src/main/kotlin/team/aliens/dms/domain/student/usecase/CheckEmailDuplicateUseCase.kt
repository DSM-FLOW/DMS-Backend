package team.aliens.dms.domain.student.usecase

import team.aliens.dms.domain.user.exception.StudentEmailExistsException
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.global.annotation.ReadOnlyUseCase

@ReadOnlyUseCase
class CheckEmailDuplicateUseCase(
    private val studentQueryUserPort: StudentQueryUserPort
) {

    fun execute(email: String) {
        if (studentQueryUserPort.existsByEmail(email)) {
            throw StudentEmailExistsException
        }
    }
}