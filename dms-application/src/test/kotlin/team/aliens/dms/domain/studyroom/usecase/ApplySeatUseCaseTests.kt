package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.studyroom.exception.SeatAlreadyAppliedException
import team.aliens.dms.domain.studyroom.exception.SeatCanNotAppliedException
import team.aliens.dms.domain.studyroom.exception.SeatNotFoundException
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.exception.StudyRoomTimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.model.AvailableTime
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.StudyRoomTimeSlot
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryAvailableTimePort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryStudentPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.UUID

class ApplySeatUseCaseTests {

    private val securityPort: StudyRoomSecurityPort = mockk(relaxed = true)
    private val queryUserPort: StudyRoomQueryUserPort = mockk(relaxed = true)
    private val queryStudentPort: StudyRoomQueryStudentPort = mockk(relaxed = true)
    private val queryStudyRoomPort: QueryStudyRoomPort = mockk(relaxed = true)
    private val commandStudyRoomPort: CommandStudyRoomPort = mockk(relaxed = true)
    private val queryAvailableTimePort: QueryAvailableTimePort = mockk(relaxed = true)

    private val applySeatUseCase = ApplySeatUseCase(
        securityPort, queryUserPort, queryStudentPort, queryStudyRoomPort, commandStudyRoomPort, queryAvailableTimePort
    )

    private val userId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val studyRoomId = UUID.randomUUID()
    private val seatId = UUID.randomUUID()
    private val timeSlotId = UUID.randomUUID()

    private val userStub by lazy {
        User(
            id = userId,
            schoolId = schoolId,
            accountId = "test account id",
            password = "test password",
            email = "test email",
            authority = Authority.STUDENT,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val studentStub by lazy {
        Student(
            id = userId,
            roomId = UUID.randomUUID(),
            roomNumber = "123",
            roomLocation = "A",
            schoolId = schoolId,
            grade = 1,
            classRoom = 3,
            number = 10,
            name = "이름",
            profileImageUrl = "https://~",
            sex = Sex.FEMALE
        )
    }

    private val seatStub by lazy {
        Seat(
            id = seatId,
            studyRoomId = studyRoomId,
            typeId = UUID.randomUUID(),
            widthLocation = 1,
            heightLocation = 1,
            number = 1,
            status = SeatStatus.AVAILABLE
        )
    }

    private val timeSlotStub by lazy {
        StudyRoomTimeSlot(
            id = timeSlotId,
            schoolId = schoolId,
            name = "10:00 ~ 10:50"
        )
    }

    private val studyRoomStub = mockk<StudyRoom>(relaxed = true)

    private val availableTimeStub: AvailableTime = mockk(relaxed = true)

    @Test
    fun `자리 신청 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudentPort.queryStudentById(userId) } returns studentStub
        every { queryStudyRoomPort.querySeatById(seatId) } returns seatStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns studyRoomStub
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns timeSlotStub
        every { studyRoomStub.schoolId } returns schoolId
        every { studyRoomStub.checkIsAvailableGradeAndSex(studentStub.grade, studentStub.sex) } returns Unit

        every { queryAvailableTimePort.queryAvailableTimeBySchoolId(schoolId) } returns availableTimeStub
        every { availableTimeStub.isAvailable() } returns true
        every { queryStudyRoomPort.existsSeatApplicationBySeatIdAndTimeSlotId(seatId, timeSlotId) } returns false

        // when & then
        assertDoesNotThrow {
            applySeatUseCase.execute(seatId, timeSlotId)
        }
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            applySeatUseCase.execute(seatId, timeSlotId)
        }
    }

    @Test
    fun `자리가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudentPort.queryStudentById(userId) } returns studentStub
        every { queryStudyRoomPort.querySeatById(seatId) } returns null

        // when & then
        assertThrows<SeatNotFoundException> {
            applySeatUseCase.execute(seatId, timeSlotId)
        }
    }

    @Test
    fun `자습실이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudentPort.queryStudentById(userId) } returns studentStub
        every { queryStudyRoomPort.querySeatById(seatId) } returns seatStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns null

        // when & then
        assertThrows<StudyRoomNotFoundException> {
            applySeatUseCase.execute(seatId, timeSlotId)
        }
    }

    @Test
    fun `자습실 이용시간이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudentPort.queryStudentById(userId) } returns studentStub
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns null

        // when & then
        assertThrows<StudyRoomTimeSlotNotFoundException> {
            applySeatUseCase.execute(seatId, timeSlotId)
        }
    }

    private val notAvailableSeatStub by lazy {
        Seat(
            id = seatId,
            studyRoomId = studyRoomId,
            typeId = UUID.randomUUID(),
            widthLocation = 1,
            heightLocation = 1,
            number = 1,
            status = SeatStatus.AVAILABLE
        )
    }

    @Test
    fun `신청할 수 없는 자리임`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudentPort.queryStudentById(userId) } returns studentStub
        every { queryStudyRoomPort.querySeatById(seatId) } returns notAvailableSeatStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns studyRoomStub
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns timeSlotStub
        every { studyRoomStub.schoolId } returns schoolId

        // when & then
        assertThrows<SeatCanNotAppliedException> {
            applySeatUseCase.execute(seatId, timeSlotId)
        }
    }

    @Test
    fun `신청할 수 없는 시간임`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudentPort.queryStudentById(userId) } returns studentStub
        every { queryStudyRoomPort.querySeatById(seatId) } returns seatStub
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns timeSlotStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns studyRoomStub
        every { studyRoomStub.schoolId } returns schoolId
        every { studyRoomStub.checkIsAvailableGradeAndSex(studentStub.grade, studentStub.sex) } returns Unit

        every { queryAvailableTimePort.queryAvailableTimeBySchoolId(schoolId) } returns availableTimeStub
        every { availableTimeStub.isAvailable() } returns false

        // when & then
        assertThrows<SeatCanNotAppliedException> {
            applySeatUseCase.execute(seatId, timeSlotId)
        }
    }

    @Test
    fun `이미 신청된 자리임`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudentPort.queryStudentById(userId) } returns studentStub
        every { queryStudyRoomPort.querySeatById(seatId) } returns seatStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns studyRoomStub

        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns timeSlotStub
        every { studyRoomStub.schoolId } returns schoolId
        every { studyRoomStub.checkIsAvailableGradeAndSex(studentStub.grade, studentStub.sex) } returns Unit

        every { queryAvailableTimePort.queryAvailableTimeBySchoolId(schoolId) } returns availableTimeStub
        every { availableTimeStub.isAvailable() } returns true
        every { queryStudyRoomPort.existsSeatApplicationBySeatIdAndTimeSlotId(seatId, timeSlotId) } returns true

        // when & then
        assertThrows<SeatAlreadyAppliedException> {
            applySeatUseCase.execute(seatId, timeSlotId)
        }
    }
}
