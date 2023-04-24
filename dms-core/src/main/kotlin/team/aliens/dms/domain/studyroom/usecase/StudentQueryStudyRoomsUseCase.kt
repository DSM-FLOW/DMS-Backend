package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomsResponse
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomsResponse.StudyRoomElement
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@ReadOnlyUseCase
class StudentQueryStudyRoomsUseCase(
    private val studentService: StudentService,
    private val studyRoomService: StudyRoomService,
) {

    fun execute(timeSlotId: UUID): StudentQueryStudyRoomsResponse {

        val student = studentService.getCurrentStudent()
        val timeSlot = studyRoomService.getTimeSlot(timeSlotId, student.schoolId)

        val appliedSeatId = studyRoomService.getAppliedSeat(student.id, timeSlot.id)?.id
        val studyRooms =
            studyRoomService.getStudyRoomVOs(timeSlot.id, student.grade, student.sex)
                .map {
                    StudyRoomElement(
                        id = it.id,
                        floor = it.floor,
                        name = it.name,
                        availableGrade = it.availableGrade,
                        availableSex = it.availableSex,
                        inUseHeadcount = it.inUseHeadcount,
                        totalAvailableSeat = it.totalAvailableSeat,
                        isMine = appliedSeatId == it.id
                    )
                }

        return StudentQueryStudyRoomsResponse(
            studyRooms = studyRooms
        )
    }
}
