package team.aliens.dms.domain.student.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.dto.TokenResponse
import team.aliens.dms.domain.auth.exception.AuthCodeMismatchException
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.room.exception.RoomNotFoundException
import team.aliens.dms.domain.room.model.Room
import team.aliens.dms.domain.school.exception.AnswerMismatchException
import team.aliens.dms.domain.school.exception.SchoolCodeMismatchException
import team.aliens.dms.domain.school.model.AvailableFeature
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.student.dto.SignUpRequest
import team.aliens.dms.domain.student.dto.SignUpResponse
import team.aliens.dms.domain.student.exception.VerifiedStudentNotFoundException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.model.VerifiedStudent
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class SignUpUseCaseTests {

    @MockBean
    private lateinit var commandStudentPort: CommandStudentPort

    @MockBean
    private lateinit var commandUserPort: StudentCommandUserPort

    @MockBean
    private lateinit var querySchoolPort: StudentQuerySchoolPort

    @MockBean
    private lateinit var queryAuthCodePort: StudentQueryAuthCodePort

    @MockBean
    private lateinit var queryUserPort: StudentQueryUserPort

    @MockBean
    private lateinit var queryVerifiedStudentPort: StudentQueryVerifiedStudentPort

    @MockBean
    private lateinit var queryRoomPort: StudentQueryRoomPort

    @MockBean
    private lateinit var securityPort: StudentSecurityPort

    @MockBean
    private lateinit var jwtPort: StudentJwtPort

    private lateinit var signUpUseCase: SignUpUseCase

    @BeforeEach
    fun setUp() {
        signUpUseCase = SignUpUseCase(
            commandStudentPort,
            commandUserPort,
            querySchoolPort,
            queryUserPort,
            queryAuthCodePort,
            queryVerifiedStudentPort,
            queryRoomPort,
            securityPort,
            jwtPort
        )
    }

    private val id = UUID.randomUUID()
    private val userId = UUID.randomUUID()
    private val code = "12345678"
    private val email = "test@test.com"
    private val accountId = "test accountId"
    private val password = "test password"
    private val question = "test question"
    private val answer = "test answer"
    private val name = "test name"
    private val profileImageUrl = "test profileImage"

    private val schoolStub by lazy {
        School(
            id = id,
            name = "test name",
            code = code,
            question = question,
            answer = answer,
            address = "주소",
            contractStartedAt = LocalDate.now(),
            contractEndedAt = null
        )
    }

    private val authCodeStub by lazy {
        AuthCode(
            code = "123412",
            email = email,
            type = EmailType.SIGNUP
        )
    }

    private val verifiedStudentStub by lazy {
        VerifiedStudent(
            id = UUID.randomUUID(),
            schoolName = schoolStub.name,
            name = name,
            roomNumber = 318,
            gcn = gcnStub,
            sex = Sex.FEMALE
        )
    }

    private val roomStub by lazy {
        Room(
            id = UUID.randomUUID(),
            number = verifiedStudentStub.roomNumber,
            schoolId = schoolStub.id
        )
    }

    private val userStub by lazy {
        User(
            id = userId,
            schoolId = schoolStub.id,
            accountId = accountId,
            password = password,
            email = email,
            authority = Authority.STUDENT,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val savedUserStub by lazy {
        User(
            id = userStub.id,
            schoolId = userStub.schoolId,
            accountId = userStub.accountId,
            password = userStub.password,
            email = userStub.email,
            authority = userStub.authority,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val studentStub by lazy {
        Student(
            id = savedUserStub.id,
            roomId = UUID.randomUUID(),
            roomNumber = 123,
            schoolId = savedUserStub.schoolId,
            grade = 1,
            classRoom = 1,
            number = 1,
            name = name,
            profileImageUrl = profileImageUrl,
            sex = Sex.FEMALE
        )
    }

    private val savedStudentStub by lazy {
        Student(
            id = studentStub.id,
            roomId = studentStub.roomId,
            roomNumber = studentStub.roomNumber,
            schoolId = studentStub.schoolId,
            grade = studentStub.grade,
            classRoom = studentStub.classRoom,
            number = studentStub.number,
            name = studentStub.name,
            profileImageUrl = studentStub.profileImageUrl,
            sex = studentStub.sex
        )
    }

    private val requestStub by lazy {
        SignUpRequest(
            schoolCode = code,
            schoolAnswer = answer,
            email = email,
            authCode = "123412",
            grade = 1,
            classRoom = 1,
            number = 1,
            accountId = accountId,
            password = password,
            profileImageUrl = profileImageUrl
        )
    }

    private val tokenResponseStub by lazy {
        TokenResponse(
            accessToken = "test access token",
            accessTokenExpiredAt = LocalDateTime.now(),
            refreshToken = "test refresh token",
            refreshTokenExpiredAt = LocalDateTime.now()
        )
    }

    private val featureStub by lazy {
        AvailableFeature(
            schoolId = userStub.schoolId,
            mealService = true,
            noticeService = true,
            pointService = true,
            studyRoomService = false
        )
    }

    private val gcnStub = "${requestStub.grade}${requestStub.classRoom}${Student.processNumber(requestStub.number)}"

    private val signUpResponseStub by lazy {
        SignUpResponse(
            accessToken = tokenResponseStub.accessToken,
            accessTokenExpiredAt = tokenResponseStub.accessTokenExpiredAt,
            refreshToken = tokenResponseStub.refreshToken,
            refreshTokenExpiredAt = tokenResponseStub.refreshTokenExpiredAt,
            features = SignUpResponse.Features(
                mealService = true,
                noticeService = true,
                pointService = true,
                studyRoomService = false
            )
        )
    }

//    @Test
//    fun `회원가입 성공`() {
//        // given
//        given(querySchoolPort.querySchoolByCode(code))
//            .willReturn(schoolStub)
//
//        given(queryUserPort.existsUserByEmail(email))
//            .willReturn(false)
//
//        given(queryAuthCodePort.queryAuthCodeByEmail(email))
//            .willReturn(authCodeStub)
//
//        given(queryUserPort.existsUserByAccountId(accountId))
//            .willReturn(false)
//
//        given(queryVerifiedStudentPort.queryVerifiedStudentByGcnAndSchoolName(gcnStub, schoolStub.name))
//            .willReturn(verifiedStudentStub)
//
//        given(queryRoomPort.queryRoomBySchoolIdAndNumber(schoolStub.id, verifiedStudentStub.roomNumber))
//            .willReturn(roomStub)
//
//        given(securityPort.encodePassword(requestStub.password))
//            .willReturn(password)
//
//        given(commandUserPort.saveUser(userStub))
//            .willReturn(savedUserStub)
//
//        given(commandStudentPort.saveStudent(studentStub))
//            .willReturn(savedStudentStub)
//
//        given(jwtPort.receiveToken(userStub.id, Authority.STUDENT))
//            .willReturn(tokenResponseStub)
//
//        given(querySchoolPort.queryAvailableFeaturesBySchoolId(userStub.schoolId))
//            .willReturn(featureStub)
//
//        // when
//        val response = signUpUseCase.execute(requestStub)
//
//        // then
//        assertThat(response).isEqualTo(signUpResponseStub)
//    }

    @Test
    fun `학교 인증코드에 해당하는 학교가 존재하지 않음`() {
        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(null)

        // when & then
        assertThrows<SchoolCodeMismatchException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `학교 확인 질문에 대한 답변이 일치하지 않음`() {
        val wrongAnswerSchool = schoolStub.copy(answer = "wrong answer")

        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(wrongAnswerSchool)

        // when & then
        assertThrows<AnswerMismatchException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `이메일이 이미 존재함`() {
        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(schoolStub)

        given(queryUserPort.existsUserByEmail(email))
            .willReturn(true)

        // when & then
        assertThrows<UserEmailExistsException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `이메일 인증코드가 존재하지 않음`() {
        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(schoolStub)

        given(queryUserPort.existsUserByEmail(email))
            .willReturn(false)

        given(queryAuthCodePort.queryAuthCodeByEmail(email))
            .willReturn(null)

        // when & then
        assertThrows<AuthCodeNotFoundException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `이메일 인증코드가 일치하지 않음`() {
        val wrongCodeAuthCode = authCodeStub.copy(code = "wrong code")

        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(schoolStub)

        given(queryUserPort.existsUserByEmail(email))
            .willReturn(false)

        given(queryAuthCodePort.queryAuthCodeByEmail(email))
            .willReturn(wrongCodeAuthCode)

        // when & then
        assertThrows<AuthCodeMismatchException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `검증된 학생 미존재`() {
        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(schoolStub)

        given(queryUserPort.existsUserByEmail(email))
            .willReturn(false)

        given(queryAuthCodePort.queryAuthCodeByEmail(email))
            .willReturn(authCodeStub)

        given(queryVerifiedStudentPort.queryVerifiedStudentByGcnAndSchoolName(gcnStub, schoolStub.name))
            .willReturn(null)

        // when & then
        assertThrows<VerifiedStudentNotFoundException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `호실 미존재`() {
        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(schoolStub)

        given(queryUserPort.existsUserByEmail(email))
            .willReturn(false)

        given(queryAuthCodePort.queryAuthCodeByEmail(email))
            .willReturn(authCodeStub)

        given(queryVerifiedStudentPort.queryVerifiedStudentByGcnAndSchoolName(gcnStub, schoolStub.name))
            .willReturn(verifiedStudentStub)

        given(queryRoomPort.queryRoomBySchoolIdAndNumber(schoolStub.id, verifiedStudentStub.roomNumber))
            .willReturn(null)

        // when & then
        assertThrows<RoomNotFoundException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `아이디가 이미 존재함`() {
        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(schoolStub)

        given(queryUserPort.existsUserByEmail(email))
            .willReturn(false)

        given(queryAuthCodePort.queryAuthCodeByEmail(email))
            .willReturn(authCodeStub)

        given(queryVerifiedStudentPort.queryVerifiedStudentByGcnAndSchoolName(gcnStub, schoolStub.name))
            .willReturn(verifiedStudentStub)

        given(queryRoomPort.queryRoomBySchoolIdAndNumber(schoolStub.id, verifiedStudentStub.roomNumber))
            .willReturn(roomStub)

        given(queryUserPort.existsUserByAccountId(accountId))
            .willReturn(true)

        // when & then
        assertThrows<UserAccountIdExistsException> {
            signUpUseCase.execute(requestStub)
        }
    }

//    @Test
//    fun `이용 가능한 기능이 존재하지 않음`() {
//        //given
//        given(querySchoolPort.querySchoolByCode(code))
//            .willReturn(schoolStub)
//
//        given(queryUserPort.existsUserByEmail(email))
//            .willReturn(false)
//
//        given(queryAuthCodePort.queryAuthCodeByEmail(email))
//            .willReturn(authCodeStub)
//
//        given(queryVerifiedStudentPort.queryVerifiedStudentByGcnAndSchoolName(gcnStub, schoolStub.name))
//            .willReturn(verifiedStudentStub)
//
//        given(queryRoomPort.queryRoomBySchoolIdAndNumber(schoolStub.id, verifiedStudentStub.roomNumber))
//            .willReturn(roomStub)
//
//        given(queryUserPort.existsUserByAccountId(accountId))
//            .willReturn(false)
//
//        given(securityPort.encodePassword(requestStub.password))
//            .willReturn(userStub.password)
//
//        given(commandUserPort.saveUser(userStub))
//            .willReturn(savedUserStub)
//
//        given(commandStudentPort.saveStudent(studentStub))
//            .willReturn(savedStudentStub)
//
//        given(jwtPort.receiveToken(id, Authority.STUDENT))
//            .willReturn(tokenResponseStub)
//
//        given(querySchoolPort.queryAvailableFeaturesBySchoolId(userStub.schoolId))
//            .willReturn(null)
//
//        // when & then
//        assertThrows<FeatureNotFoundException> {
//            signUpUseCase.execute(requestStub)
//        }
//    }
}