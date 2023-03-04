package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.auth.exception.AuthCodeMismatchException
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.room.exception.RoomNotFoundException
import team.aliens.dms.domain.school.exception.AnswerMismatchException
import team.aliens.dms.domain.school.exception.FeatureNotFoundException
import team.aliens.dms.domain.school.exception.SchoolCodeMismatchException
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.student.dto.SignUpRequest
import team.aliens.dms.domain.student.dto.SignUpResponse
import team.aliens.dms.domain.student.exception.VerifiedStudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentJwtPort
import team.aliens.dms.domain.student.spi.StudentQueryAuthCodePort
import team.aliens.dms.domain.student.spi.StudentQueryRoomPort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.spi.StudentQueryVerifiedStudentPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.user.exception.UserAccountIdExistsException
import team.aliens.dms.domain.user.exception.UserEmailExistsException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.UUID

/**
 *
 * 학생이 회원가입을 하는 SignUpUseCase
 *
 * @author kimbeomjin, leejeongyoon
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
    private val queryVerifiedStudentPort: StudentQueryVerifiedStudentPort,
    private val queryRoomPort: StudentQueryRoomPort,
    private val securityPort: StudentSecurityPort,
    private val jwtPort: StudentJwtPort
) {

    fun execute(request: SignUpRequest): SignUpResponse {
        val (
            schoolCode, schoolAnswer, authCode,
            grade, classRoom, number,
            accountId, password, email, profileImageUrl
        ) = request

        val school = validateSchool(schoolCode, schoolAnswer)

        validateAuthCode(authCode, email)
        validateUserDuplicated(accountId, email)

        /**
         * 검증된 학생 조회
         **/
        val verifiedStudent = queryVerifiedStudentPort.queryVerifiedStudentByGcnAndSchoolName(
            gcn = Student.processGcn(
                grade = grade,
                classRoom = classRoom,
                number = number
            ),
            schoolName = school.name
        ) ?: throw VerifiedStudentNotFoundException

        /**
         * 호실 조회
         **/
        val room = queryRoomPort.queryRoomBySchoolIdAndNumber(
            schoolId = school.id,
            number = verifiedStudent.roomNumber
        ) ?: throw RoomNotFoundException

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
            roomId = room.id,
            roomNumber = room.number,
            roomLocation = verifiedStudent.roomLocation,
            schoolId = school.id,
            grade = grade,
            classRoom = classRoom,
            number = number,
            name = verifiedStudent.name,
            profileImageUrl = profileImageUrl ?: Student.PROFILE_IMAGE,
            sex = verifiedStudent.sex
        )
        commandStudentPort.saveStudent(student)
        commandStudentPort.deleteVerifiedStudent(verifiedStudent)

        val (accessToken, accessTokenExpiredAt, refreshToken, refreshTokenExpiredAt) = jwtPort.receiveToken(
            userId = user.id, authority = Authority.STUDENT
        )

        val availableFeatures = querySchoolPort.queryAvailableFeaturesBySchoolId(user.schoolId)
            ?: throw FeatureNotFoundException

        return SignUpResponse(
            accessToken = accessToken,
            accessTokenExpiredAt = accessTokenExpiredAt,
            refreshToken = refreshToken,
            refreshTokenExpiredAt = refreshTokenExpiredAt,
            features = availableFeatures.run {
                SignUpResponse.Features(
                    mealService = mealService,
                    noticeService = noticeService,
                    pointService = pointService,
                    studyRoomService = studyRoomService,
                )
            }
        )
    }

    private fun validateAuthCode(authCode: String, email: String) {
        /**
         * 이메일 인증코드 검사
         **/
        val authCodeEntity = queryAuthCodePort.queryAuthCodeByEmail(email) ?: throw AuthCodeNotFoundException

        if (authCode != authCodeEntity.code) {
            throw AuthCodeMismatchException
        }
    }

    private fun validateSchool(schoolCode: String, schoolAnswer: String): School {
        val school = querySchoolPort.querySchoolByCode(schoolCode) ?: throw SchoolCodeMismatchException

        /**
         * 학교 확인 질문 답변 검사
         **/
        if (school.answer != schoolAnswer) {
            throw AnswerMismatchException
        }

        return school
    }

    private fun validateUserDuplicated(accountId: String, email: String) {
        /**
         * 아이디 중복 검사
         **/
        if (queryUserPort.existsUserByAccountId(accountId)) {
            throw UserAccountIdExistsException
        }

        /**
         * 이메일 중복 검사
         **/
        if (queryUserPort.existsUserByEmail(email)) {
            throw UserEmailExistsException
        }
    }

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
        createdAt = LocalDateTime.now(),
        deletedAt = null
    )
}
