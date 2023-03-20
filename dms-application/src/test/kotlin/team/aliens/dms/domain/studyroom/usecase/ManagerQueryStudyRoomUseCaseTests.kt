package team.aliens.dms.domain.studyroom.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.spi.vo.SeatVO
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
import java.util.UUID

@ExtendWith(SpringExtension::class)
class ManagerQueryStudyRoomUseCaseTests {

    @MockBean
    private lateinit var securityPort: StudyRoomSecurityPort

    @MockBean
    private lateinit var queryUserPort: StudyRoomQueryUserPort

    @MockBean
    private lateinit var queryStudyRoomPort: QueryStudyRoomPort

    private lateinit var managerQueryStudyRoomUseCase: ManagerQueryStudyRoomUseCase

    @BeforeEach
    fun setUp() {
        managerQueryStudyRoomUseCase = ManagerQueryStudyRoomUseCase(
            securityPort, queryUserPort, queryStudyRoomPort
        )
    }

    private val studyRoomId = UUID.randomUUID()
    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val studentId = UUID.randomUUID()

    private val userStub by lazy {
        createUserStub(id = currentUserId, schoolId = schoolId)
    }

    private val studyRoomStub by lazy {
        StudyRoom(
            id = studyRoomId,
            schoolId = schoolId,
            name = "이름",
            floor = 1,
            widthSize = 1,
            heightSize = 1,
            inUseHeadcount = 1,
            availableHeadcount = 1,
            availableSex = Sex.ALL,
            availableGrade = 1,
            eastDescription = "동쪽",
            westDescription = "서쪽",
            southDescription = "남쪽",
            northDescription = "북쪽"
        )
    }

    private val managerSeatVOStub by lazy {
        SeatVO(
            seatId = UUID.randomUUID(),
            widthLocation = 1,
            heightLocation = 1,
            number = 1,
            status = SeatStatus.AVAILABLE,
            typeId = UUID.randomUUID(),
            typeName = "타입 이름",
            typeColor = "색깔",
            studentId = studentId,
            studentName = "학생 이름",
            studentGrade = 1,
            studentClassRoom = 1,
            studentNumber = 1,
            studentProfileImageUrl = "https://~"
        )
    }

    @Test
    fun `자습실 조회 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllSeatsByStudyRoomId(studyRoomId))
            .willReturn(listOf(managerSeatVOStub))

        // when & then
        assertDoesNotThrow {
            managerQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `자리 상태 신청 가능`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllSeatsByStudyRoomId(studyRoomId))
            .willReturn(
                listOf(
                    managerSeatVOStub.run {
                        SeatVO(
                            seatId = seatId,
                            widthLocation = widthLocation,
                            heightLocation = heightLocation,
                            number = number,
                            status = SeatStatus.AVAILABLE,
                            typeId = typeId,
                            typeName = typeName,
                            typeColor = typeColor,
                            studentId = null,
                            studentName = null,
                            studentGrade = null,
                            studentClassRoom = null,
                            studentNumber = null,
                            studentProfileImageUrl = null
                        )
                    }
                )
            )

        // when & then
        assertDoesNotThrow {
            managerQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `자리 상태 신청 불가능`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllSeatsByStudyRoomId(studyRoomId))
            .willReturn(
                listOf(
                    managerSeatVOStub.run {
                        SeatVO(
                            seatId = seatId,
                            widthLocation = widthLocation,
                            heightLocation = heightLocation,
                            number = null,
                            status = SeatStatus.UNAVAILABLE,
                            typeId = null,
                            typeName = null,
                            typeColor = null,
                            studentId = null,
                            studentName = null,
                            studentGrade = null,
                            studentClassRoom = null,
                            studentNumber = null,
                            studentProfileImageUrl = null
                        )
                    }
                )
            )

        // when & then
        assertDoesNotThrow {
            managerQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `자리 상태 사용중`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllSeatsByStudyRoomId(studyRoomId))
            .willReturn(
                listOf(
                    managerSeatVOStub.run {
                        SeatVO(
                            seatId = seatId,
                            widthLocation = widthLocation,
                            heightLocation = heightLocation,
                            number = number,
                            status = SeatStatus.IN_USE,
                            typeId = typeId,
                            typeName = typeName,
                            typeColor = typeColor,
                            studentId = studentId,
                            studentName = studentName,
                            studentGrade = studentGrade,
                            studentClassRoom = studentClassRoom,
                            studentNumber = studentNumber,
                            studentProfileImageUrl = studentProfileImageUrl
                        )
                    }
                )
            )

        // when & then
        assertDoesNotThrow {
            managerQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `자리 상태 빈 자리`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllSeatsByStudyRoomId(studyRoomId))
            .willReturn(
                listOf(
                    managerSeatVOStub.run {
                        SeatVO(
                            seatId = seatId,
                            widthLocation = widthLocation,
                            heightLocation = heightLocation,
                            number = null,
                            status = SeatStatus.EMPTY,
                            typeId = null,
                            typeName = null,
                            typeColor = null,
                            studentId = null,
                            studentName = null,
                            studentGrade = null,
                            studentClassRoom = null,
                            studentNumber = null,
                            studentProfileImageUrl = null
                        )
                    }
                )
            )

        // when & then
        assertDoesNotThrow {
            managerQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `사용자 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            managerQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `자습실 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(null)

        // when & then
        assertThrows<StudyRoomNotFoundException> {
            managerQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `학교 불일치`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub.copy(schoolId = UUID.randomUUID()))

        // when & then
        assertThrows<SchoolMismatchException> {
            managerQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }
}
