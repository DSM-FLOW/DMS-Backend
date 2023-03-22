package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse.SeatElement
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse.SeatElement.StudentElement
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse.SeatElement.TypeElement
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@ReadOnlyUseCase
class StudentQueryStudyRoomUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val queryStudyRoomPort: QueryStudyRoomPort
) {

    fun execute(studyRoomId: UUID, timeSlotId: UUID): StudentQueryStudyRoomResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val studyRoom = queryStudyRoomPort.queryStudyRoomById(studyRoomId) ?: throw StudyRoomNotFoundException
        validateSameSchool(user.schoolId, studyRoom.schoolId)

        val timeSlot = queryStudyRoomPort.queryTimeSlotById(timeSlotId) ?: throw TimeSlotNotFoundException
        validateSameSchool(user.schoolId, timeSlot.schoolId)

        val seats = queryStudyRoomPort.queryAllSeatApplicationVOsByStudyRoomIdAndTimeSlotId(studyRoomId, timeSlotId).map {
            SeatElement(
                id = it.seatId,
                widthLocation = it.widthLocation,
                heightLocation = it.heightLocation,
                number = it.number,
                type = it.typeId?.run {
                    TypeElement(
                        id = it.typeId,
                        name = it.typeName!!,
                        color = it.typeColor!!
                    )
                },
                status = it.status,
                isMine = this.isMine(
                    studentId = it.studentId,
                    currentUserId = currentUserId,
                    status = it.status
                ),
                student = it.studentId?.run {
                    StudentElement(
                        id = it.studentId,
                        name = it.studentName!!
                    )
                }
            )
        }

        return studyRoom.run {
            StudentQueryStudyRoomResponse(
                floor = floor,
                name = name,
                startTime = timeSlot.startTime,
                endTime = timeSlot.endTime,
                totalAvailableSeat = availableHeadcount,
                inUseHeadcount = seats.count { it.student != null },
                availableSex = availableSex,
                availableGrade = availableGrade,
                eastDescription = eastDescription,
                westDescription = westDescription,
                southDescription = southDescription,
                northDescription = northDescription,
                totalWidthSize = widthSize,
                totalHeightSize = heightSize,
                seats = seats
            )
        }
    }

    private fun isMine(studentId: UUID?, currentUserId: UUID, status: SeatStatus) = studentId?.run {
        studentId == currentUserId
    } ?: run {
        /**
         * student_id 가 NULL 일 경우
         *
         * AVAILABLE -> false
         * UNAVAILABLE -> NULL
         * EMPTY -> NULL
         **/
        when (status) {
            SeatStatus.AVAILABLE -> false
            else -> null
        }
    }
}
