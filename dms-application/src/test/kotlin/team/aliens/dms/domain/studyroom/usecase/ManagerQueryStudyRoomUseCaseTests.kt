package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import java.time.LocalTime
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.exception.StudyRoomTimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.stub.createStudyRoomStub
import team.aliens.dms.domain.studyroom.stub.createTimeSlotStub
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub

class ManagerQueryStudyRoomUseCaseTests {

    private val securityPort: StudyRoomSecurityPort = mockk(relaxed = true)
    private val queryUserPort: StudyRoomQueryUserPort = mockk(relaxed = true)
    private val queryStudyRoomPort: QueryStudyRoomPort = mockk(relaxed = true)

    private val managerQueryRoomUseCase = ManagerQueryStudyRoomUseCase(
        securityPort, queryUserPort, queryStudyRoomPort
    )

    private val userId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val studyRoomId = UUID.randomUUID()
    private val timeSlotId = UUID.randomUUID()

    private val userStub by lazy {
        createUserStub(
            id = userId,
            schoolId = schoolId
        )
    }

    private val studyRoomStub by lazy {
        createStudyRoomStub(
            id = studyRoomId,
            schoolId = schoolId
        )
    }

    private val timeSlotStub by lazy {
        createTimeSlotStub(
            id = timeSlotId,
            schoolId = schoolId
        )
    }

    @Test
    fun `자습실 조회 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns studyRoomStub
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns timeSlotStub
        every { queryStudyRoomPort.queryTimeSlotsBySchoolIdAndStudyRoomId(schoolId, studyRoomId) } returns listOf(timeSlotStub)

        // when & then
        assertDoesNotThrow {
            managerQueryRoomUseCase.execute(studyRoomId, timeSlotId)
        }
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            managerQueryRoomUseCase.execute(studyRoomId, timeSlotId)
        }
    }

    @Test
    fun `자습실이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns null

        // when & then
        assertThrows<StudyRoomNotFoundException> {
            managerQueryRoomUseCase.execute(studyRoomId, timeSlotId)
        }
    }

    @Test
    fun `이용시간이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns studyRoomStub
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns null

        // when & then
        assertThrows<TimeSlotNotFoundException> {
            managerQueryRoomUseCase.execute(studyRoomId, timeSlotId)
        }
    }

    @Test
    fun `자습실에 대한 이용시간이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns studyRoomStub
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns timeSlotStub
        every { queryStudyRoomPort.queryTimeSlotsBySchoolIdAndStudyRoomId(schoolId, studyRoomId) } returns listOf()

        // when & then
        assertThrows<StudyRoomTimeSlotNotFoundException> {
            managerQueryRoomUseCase.execute(studyRoomId, timeSlotId)
        }
    }

    private val otherUserId = UUID.randomUUID()
    private val otherUserStub by lazy {
        createUserStub(
            id = otherUserId,
            schoolId = schoolId
        )
    }

    @Test
    fun `다른 학교의 자습실임`() {
        // given
        every { securityPort.getCurrentUserId() } returns otherUserId
        every { queryUserPort.queryUserById(userId) } returns otherUserStub
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns timeSlotStub
        every { queryStudyRoomPort.queryTimeSlotsBySchoolIdAndStudyRoomId(schoolId, studyRoomId) } returns listOf(timeSlotStub)
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns studyRoomStub

        // when & then
        assertThrows<SchoolMismatchException> {
            managerQueryRoomUseCase.execute(studyRoomId, timeSlotId)
        }
    }

    private val otherTimeSlotStub by lazy {
        TimeSlot(
            id = timeSlotId,
            schoolId = UUID.randomUUID(),
            startTime = LocalTime.of(0, 0),
            endTime = LocalTime.of(0, 0)
        )
    }

    @Test
    fun `다른 학교의 이용시간임`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns studyRoomStub
        every { queryStudyRoomPort.queryTimeSlotsBySchoolIdAndStudyRoomId(schoolId, studyRoomId) } returns listOf(otherTimeSlotStub)
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns otherTimeSlotStub

        // when & then
        assertThrows<SchoolMismatchException> {
            managerQueryRoomUseCase.execute(studyRoomId, timeSlotId)
        }
    }
}
