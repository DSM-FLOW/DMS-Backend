package team.aliens.dms.domain.manager.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerCommandStudentPort
import team.aliens.dms.domain.manager.spi.ManagerCommandUserPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class RemoveStudentUseCaseTests {

    @MockBean
    private lateinit var securityPort: ManagerSecurityPort

    @MockBean
    private lateinit var queryUserPort: ManagerQueryUserPort

    @MockBean
    private lateinit var queryStudentPort: ManagerQueryStudentPort

    @MockBean
    private lateinit var commandStudentPort: ManagerCommandStudentPort

    @MockBean
    private lateinit var commandUserPort: ManagerCommandUserPort

    private lateinit var removeStudentUseCase: RemoveStudentUseCase

    @BeforeEach
    fun setUp() {
        removeStudentUseCase = RemoveStudentUseCase(
            securityPort, queryUserPort, queryStudentPort, commandStudentPort, commandUserPort
        )
    }

    private val userStub by lazy {
        User(
            id = UUID.randomUUID(),
            schoolId = schoolId,
            accountId = "아이디",
            password = "비밀번호",
            email = "이메일",
            name = "이름",
            profileImageUrl = "https://~",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val studentStub by lazy {
        Student(
            studentId = studentId,
            roomNumber = 318,
            schoolId = schoolId,
            grade = 2,
            classRoom = 3,
            number = 10
        )
    }

    private val managerId = UUID.randomUUID()
    private val studentId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    @Test
    fun `학생 삭제 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        given(queryUserPort.queryUserById(managerId))
            .willReturn(userStub)

        given(queryStudentPort.queryStudentById(studentId))
            .willReturn(studentStub)

        given(queryUserPort.queryUserById(studentStub.studentId))
            .willReturn(userStub)

        given(commandUserPort.saveUser(userStub.copy(deletedAt = LocalDateTime.now())))
            .willReturn(userStub)

        // when & then
        assertDoesNotThrow {
            removeStudentUseCase.execute(studentId)
        }
    }

    @Test
    fun `관리자 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        given(queryUserPort.queryUserById(managerId))
            .willReturn(null)

        // when & then
        assertThrows<ManagerNotFoundException> {
            removeStudentUseCase.execute(studentId)
        }
    }

    @Test
    fun `학생 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        given(queryUserPort.queryUserById(managerId))
            .willReturn(userStub)

        given(queryStudentPort.queryStudentById(studentId))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            removeStudentUseCase.execute(studentId)
        }
    }

    @Test
    fun `학생 유저 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        given(queryUserPort.queryUserById(managerId))
            .willReturn(userStub)

        given(queryStudentPort.queryStudentById(studentId))
            .willReturn(studentStub)

        given(queryUserPort.queryUserById(studentStub.studentId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            removeStudentUseCase.execute(studentId)
        }
    }

    @Test
    fun `학교 불일치`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        given(queryUserPort.queryUserById(managerId))
            .willReturn(userStub.copy(schoolId = UUID.randomUUID()))

        given(queryStudentPort.queryStudentById(studentId))
            .willReturn(studentStub)

        given(queryUserPort.queryUserById(studentStub.studentId))
            .willReturn(userStub)

        // when & then
        assertThrows<SchoolMismatchException> {
            removeStudentUseCase.execute(studentId)
        }
    }
}