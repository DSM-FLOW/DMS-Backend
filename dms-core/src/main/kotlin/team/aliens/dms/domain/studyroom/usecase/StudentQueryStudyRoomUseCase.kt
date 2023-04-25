package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse.SeatElement
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse.SeatElement.StudentElement
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse.SeatElement.TypeElement
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import java.util.UUID

@ReadOnlyUseCase
class StudentQueryStudyRoomUseCase(
    private val studentService: StudentService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(studyRoomId: UUID, timeSlotId: UUID): StudentQueryStudyRoomResponse {

        val student = studentService.getCurrentStudent()

        val studyRoom = studyRoomService.getStudyRoom(studyRoomId, student.schoolId)

        val timeSlot = studyRoomService.getTimeSlot(timeSlotId, student.schoolId)
        studyRoomService.checkStudyRoomTimeSlotExistsById(studyRoomId, timeSlotId)

        val seats = studyRoomService.getSeatApplicationVOs(studyRoomId, timeSlotId).map {
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
                    currentStudentId = student.id,
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

    private fun isMine(studentId: UUID?, currentStudentId: UUID, status: SeatStatus) = studentId?.run {
        studentId == currentStudentId
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
