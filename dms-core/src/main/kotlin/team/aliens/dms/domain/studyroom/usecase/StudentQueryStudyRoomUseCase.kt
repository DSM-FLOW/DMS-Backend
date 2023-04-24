package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse.SeatElement
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse.SeatElement.StudentElement
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse.SeatElement.TypeElement
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.exception.StudyRoomTimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import java.util.UUID

@ReadOnlyUseCase
class StudentQueryStudyRoomUseCase(
    private val studentService: StudentService,
    private val queryStudyRoomPort: QueryStudyRoomPort
) {

    fun execute(studyRoomId: UUID, timeSlotId: UUID): StudentQueryStudyRoomResponse {

        val student = studentService.getCurrentStudent()

        val studyRoom = queryStudyRoomPort.queryStudyRoomById(studyRoomId) ?: throw StudyRoomNotFoundException
        validateSameSchool(student.schoolId, studyRoom.schoolId)

        if (!queryStudyRoomPort.existsStudyRoomTimeSlotByStudyRoomIdAndTimeSlotId(studyRoomId, timeSlotId)) {
            throw StudyRoomTimeSlotNotFoundException
        }

        val timeSlot = queryStudyRoomPort.queryTimeSlotById(timeSlotId) ?: throw TimeSlotNotFoundException
        validateSameSchool(student.schoolId, timeSlot.schoolId)

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
