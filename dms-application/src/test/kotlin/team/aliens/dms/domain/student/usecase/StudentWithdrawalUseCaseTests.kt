package team.aliens.dms.domain.student.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.student.spi.StudentCommandRemainStatusPort
import team.aliens.dms.domain.student.spi.StudentCommandStudyRoomPort
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentQueryStudyRoomPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.util.UUID

@ExtendWith(SpringExtension::class)
class StudentWithdrawalUseCaseTests {

    private val securityPort: StudentSecurityPort = mockk(relaxed = true)
    private val queryUserPort: StudentQueryUserPort = mockk(relaxed = true)
    private val commandRemainStatusPort: StudentCommandRemainStatusPort = mockk(relaxed = true)
    private val queryStudyRoomPort: StudentQueryStudyRoomPort = mockk(relaxed = true)
    private val commandStudyRoomPort: StudentCommandStudyRoomPort = mockk(relaxed = true)
    private val commandUserPort: StudentCommandUserPort = mockk(relaxed = true)

    private val studentWithdrawalUseCase = StudentWithdrawalUseCase(
        securityPort, queryUserPort, commandRemainStatusPort, queryStudyRoomPort, commandStudyRoomPort, commandUserPort
    )

    private val currentStudentId = UUID.randomUUID()

    private val userStub by lazy {
        User(
            id = currentStudentId,
            schoolId = UUID.randomUUID(),
            accountId = "",
            password = "",
            email = "email",
            authority = Authority.STUDENT,
            createdAt = null,
            deletedAt = null
        )
    }

    private val studyRoomId = UUID.randomUUID()

    private val seatStub by lazy {
        Seat(
            id = UUID.randomUUID(),
            studyRoomId = studyRoomId,
            studentId = UUID.randomUUID(),
            typeId = UUID.randomUUID(),
            widthLocation = 1,
            heightLocation = 1,
            number = 1,
            status = SeatStatus.AVAILABLE
        )
    }

    @Test
    fun `학생 탈퇴 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentStudentId

        every { queryUserPort.queryUserById(currentStudentId) } returns userStub

        every { queryStudyRoomPort.querySeatByStudentId(currentStudentId) } returns null

        // when & then
        assertDoesNotThrow {
            studentWithdrawalUseCase.execute()
        }
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentStudentId

        every { queryUserPort.queryUserById(currentStudentId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            studentWithdrawalUseCase.execute()
        }
    }

    @Test
    fun `자습실이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentStudentId

        every { queryUserPort.queryUserById(currentStudentId) } returns userStub

        every { queryStudyRoomPort.querySeatByStudentId(currentStudentId) } returns seatStub

        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns null

        // when & then
        assertThrows<StudyRoomNotFoundException> {
            studentWithdrawalUseCase.execute()
        }
    }
}
