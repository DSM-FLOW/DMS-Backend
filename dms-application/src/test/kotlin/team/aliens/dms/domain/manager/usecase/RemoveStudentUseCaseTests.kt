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
import team.aliens.dms.domain.manager.spi.ManagerCommandUserPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.StudentCommandRemainStatusPort
import team.aliens.dms.domain.student.spi.StudentCommandStudyRoomPort
import team.aliens.dms.domain.student.spi.StudentQueryStudyRoomPort
import team.aliens.dms.domain.student.stub.createStudentStub
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
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
    private lateinit var commandRemainStatusPort: StudentCommandRemainStatusPort

    @MockBean
    private lateinit var commandStudyRoomPort: StudentCommandStudyRoomPort

    @MockBean
    private lateinit var commandStudentPort: CommandStudentPort

    @MockBean
    private lateinit var commandUserPort: ManagerCommandUserPort

    private lateinit var removeStudentUseCase: RemoveStudentUseCase

    @BeforeEach
    fun setUp() {
        removeStudentUseCase = RemoveStudentUseCase(
            securityPort, queryUserPort, queryStudentPort, commandRemainStatusPort,
            commandStudyRoomPort, commandStudentPort, commandUserPort
        )
    }

    private val userStub by lazy {
        createUserStub(schoolId = schoolId)
    }

    private val studentStub by lazy {
        createStudentStub(
            id = studentId,
            schoolId = schoolId
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

        given(queryUserPort.queryUserById(studentStub.id))
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

        given(queryUserPort.queryUserById(studentStub.id))
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

        given(queryUserPort.queryUserById(studentStub.id))
            .willReturn(userStub)

        // when & then
        assertThrows<SchoolMismatchException> {
            removeStudentUseCase.execute(studentId)
        }
    }
}
