package team.aliens.dms.domain.school.usecase

import team.aliens.dms.domain.school.exception.AnswerNotMatchedException
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.global.annotation.ReadOnlyUseCase
import java.util.*

@ReadOnlyUseCase
class CheckSchoolAnswerUseCase(
    private val querySchoolPort: QuerySchoolPort
) {

    fun execute(schoolId: UUID, answer: String) {
        val school = querySchoolPort.querySchoolById(schoolId) ?: throw SchoolNotFoundException

        if (school.answer != answer) {
            throw AnswerNotMatchedException
        }
    }

}