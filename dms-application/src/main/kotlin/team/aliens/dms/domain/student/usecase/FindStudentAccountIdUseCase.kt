package team.aliens.dms.domain.student.usecase

import team.aliens.dms.domain.student.exception.StudentInfoNotMatchedException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.global.annotation.ReadOnlyUseCase
import team.aliens.dms.global.spi.CoveredEmailPort
import java.util.UUID

/**
 *
 * 이름과 학번의 정보가 소속된 학교의 학생 정보와 일치한다면 블록처리된 이메일을 반환하는 FindStudentAccountIdUseCase
 *
 * @author leejeongyoon
 * @date 2022/10/19
 * @version 1.0.0
 **/
@ReadOnlyUseCase
class FindStudentAccountIdUseCase(
    private val queryStudentPort: QueryStudentPort,
    private val queryUserPort: StudentQueryUserPort,
    private val coveredEmailPort: CoveredEmailPort
) {

    fun execute(schoolId: UUID, name: String, grade: Int, classRoom: Int, number: Int): String {
        val student = queryStudentPort.queryStudentBySchoolIdAndGcn(
            schoolId, grade, classRoom, number
        ) ?: throw StudentNotFoundException
        
        val user = queryUserPort.queryByUserId(student.studentId) ?: throw UserNotFoundException

        if (user.name == name && !queryStudentPort.existsByGcn(grade, classRoom, number)) {
            throw StudentInfoNotMatchedException
        }

        return coveredEmailPort.coveredEmail(user.email)
    }
}