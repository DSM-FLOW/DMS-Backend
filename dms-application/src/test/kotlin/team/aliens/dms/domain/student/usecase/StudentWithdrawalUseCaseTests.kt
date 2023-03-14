package team.aliens.dms.domain.student.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentCommandRemainStatusPort
import team.aliens.dms.domain.student.spi.StudentCommandStudyRoomPort
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.util.UUID

@ExtendWith(SpringExtension::class)
class StudentWithdrawalUseCaseTests {

    private val securityPort: StudentSecurityPort = mockk(relaxed = true)
    private val queryStudentPort: QueryStudentPort = mockk(relaxed = true)
    private val queryUserPort: StudentQueryUserPort = mockk(relaxed = true)
    private val commandRemainStatusPort: StudentCommandRemainStatusPort = mockk(relaxed = true)
    private val commandStudyRoomPort: StudentCommandStudyRoomPort = mockk(relaxed = true)
    private val commandStudentPort: CommandStudentPort = mockk(relaxed = true)
    private val commandUserPort: StudentCommandUserPort = mockk(relaxed = true)

    private val studentWithdrawalUseCase = StudentWithdrawalUseCase(
        securityPort, queryStudentPort, queryUserPort, commandRemainStatusPort,
        commandStudyRoomPort, commandStudentPort, commandUserPort
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

    private val studentStub by lazy {
        Student(
            id = currentStudentId,
            roomId = UUID.randomUUID(),
            roomNumber = "216",
            roomLocation = "A",
            schoolId = UUID.randomUUID(),
            grade = 2,
            classRoom = 1,
            number = 20,
            name = "김범진",
            profileImageUrl = "profile image url",
            sex = Sex.FEMALE
        )
    }

    private val studyRoomId = UUID.randomUUID()

    private val seatStub by lazy {
        Seat(
            id = UUID.randomUUID(),
            studyRoomId = studyRoomId,
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

        every { queryStudentPort.queryStudentById(currentStudentId) } returns studentStub

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
    fun `학생이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentStudentId

        every { queryUserPort.queryUserById(currentStudentId) } returns userStub

        every { queryStudentPort.queryStudentById(currentStudentId) } returns null

        // when & then
        assertThrows<StudentNotFoundException> {
            studentWithdrawalUseCase.execute()
        }
    }
}
