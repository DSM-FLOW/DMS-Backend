package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.auth.exception.AuthCodeMismatchException
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.school.exception.AnswerMismatchException
import team.aliens.dms.domain.school.exception.SchoolCodeMismatchException
import team.aliens.dms.domain.student.dto.SignUpResponse
import team.aliens.dms.domain.student.dto.SignUpRequest
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentJwtPort
import team.aliens.dms.domain.student.spi.StudentQueryAuthCodePort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.user.exception.UserAccountIdExistsException
import team.aliens.dms.domain.user.exception.UserEmailExistsException
import team.aliens.dms.domain.user.model.User
import java.util.UUID

/**
 *
 * 학생이 회원가입을 하는 SignUpUseCase
 *
 * @author kimbeomjin
 * @date 2022/10/22
 * @version 1.0.0
 **/
@UseCase
class SignUpUseCase(
    private val commandStudentPort: CommandStudentPort,
    private val commandUserPort: StudentCommandUserPort,
    private val querySchoolPort: StudentQuerySchoolPort,
    private val queryUserPort: StudentQueryUserPort,
    private val queryAuthCodePort: StudentQueryAuthCodePort,
    private val securityPort: StudentSecurityPort,
    private val jwtPort: StudentJwtPort
) {

    fun execute(request: SignUpRequest): SignUpResponse {
        val (
            schoolCode, schoolAnswer, authCode,
            grade, classRoom, number,
            accountId, password, email, profileImageUrl
        ) = request

        val school = querySchoolPort.querySchoolByCode(schoolCode) ?: throw SchoolCodeMismatchException

        /**
         * 학교 확인 질문 답변 검사
         **/
        if (school.answer != schoolAnswer) {
            throw AnswerMismatchException
        }

        /**
         * 이메일 중복 검사
         **/
        if (queryUserPort.existsUserByEmail(email)) {
            throw UserEmailExistsException
        }

        /**
         * 이메일 인증코드 검사
         **/
        val authCodeEntity = queryAuthCodePort.queryAuthCodeByEmail(email) ?: throw AuthCodeNotFoundException

        if (authCode != authCodeEntity.code) {
            throw AuthCodeMismatchException
        }

        // TODO 학번으로 이름, 호실 조회

        /**
         * 아이디 중복 검사
         **/
        if (queryUserPort.existsUserByAccountId(accountId)) {
            throw UserAccountIdExistsException
        }

        val user = commandUserPort.saveUser(
            createUser(
                schoolId = school.id,
                accountId = accountId,
                password = password,
                email = email
            )
        )

        val student = Student(
            id = user.id,
            roomId = UUID(0, 0), // TODO 학번으로 조회한 호실
            roomNumber = "${grade}${classRoom}${processedNumber(number)}".toInt(),
            schoolId = school.id,
            grade = grade,
            classRoom = classRoom,
            number = number,
            name = "", // TODO 이름,
            profileImageUrl = profileImageUrl
        )
        commandStudentPort.saveStudent(student)

        val (accessToken, expiredAt, refreshToken) = jwtPort.receiveToken(user.id, Authority.STUDENT)

        return SignUpResponse(
            accessToken = accessToken,
            expiredAt = expiredAt,
            refreshToken = refreshToken,
            features = SignUpResponse.Features(
                // TODO 서비스 관리 테이블 필요
                mealService = true,
                noticeService = true,
                pointService = true,
            )
        )
    }

    private fun processedNumber(number: Int): Int = if (number < 10) "0${number}".toInt() else number

    private fun createUser(
        schoolId: UUID,
        accountId: String,
        password: String,
        email: String
    ) = User(
        schoolId = schoolId,
        accountId = accountId,
        password = securityPort.encodePassword(password),
        email = email,
        authority = Authority.STUDENT,
        createdAt = null,
        deletedAt = null
    )
}