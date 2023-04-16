package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.spi.SendEmailPort
import team.aliens.dms.domain.school.exception.AnswerMismatchException
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@ReadOnlyUseCase
class FindManagerAccountIdUseCase(
    private val querySchoolPort: QuerySchoolPort,
    private val userService: UserService,
    private val sendEmailPort: SendEmailPort
) {

    fun execute(schoolId: UUID, answer: String): String {
        val school = querySchoolPort.querySchoolById(schoolId) ?: throw SchoolNotFoundException

        if (school.answer != answer) {
            throw AnswerMismatchException
        }

        val user = userService.queryUserBySchoolIdAndAuthority(schoolId, Authority.MANAGER)

        sendEmailPort.sendAccountId(user.email, user.accountId)
        return StringUtil.coveredEmail(user.email)
    }
}
