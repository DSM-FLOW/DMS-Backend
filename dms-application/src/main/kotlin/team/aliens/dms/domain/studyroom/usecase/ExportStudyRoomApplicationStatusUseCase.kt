package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.file.model.File
import team.aliens.dms.domain.file.spi.WriteFilePort
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.studyroom.dto.ExportStudyRoomApplicationStatusResponse
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQuerySchoolPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryStudentPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatInfo
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.time.LocalDateTime

@ReadOnlyUseCase
class ExportStudyRoomApplicationStatusUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val querySchoolPort: StudyRoomQuerySchoolPort,
    private val queryStudentPort: StudyRoomQueryStudentPort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val writeFilePort: WriteFilePort,
) {

    fun execute(file: java.io.File?): ExportStudyRoomApplicationStatusResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val students = queryStudentPort.queryStudentsBySchoolId(manager.schoolId)
        val studentSeatApplicationsMap = queryStudyRoomPort.querySeatApplicationsByStudentIdIn(
            studentIds = students.map { it.id }
        ).associateBy { it.studentId }

        val studentSeats = students.map { student ->
            val seat = studentSeatApplicationsMap[student.id]
            StudentSeatInfo(
                studentId = student.id,
                studentName = student.name,
                studentGrade = student.grade,
                studentClassRoom = student.classRoom,
                studentNumber = student.number,
                seatFullName = seat?.let {
                    StudyRoom.precessFullName(it.studyRoomFloor, it.studyRoomName) +
                        Seat.processFullName(it.seatNumber, it.seatTypeName)
                },
                timeSlotId = seat?.timeSlotId
            )
        }

        val timeSlots = queryStudyRoomPort.queryTimeSlotsBySchoolId(manager.schoolId)
        val school = querySchoolPort.querySchoolById(manager.schoolId) ?: throw SchoolNotFoundException

        return ExportStudyRoomApplicationStatusResponse(
            file = file?.let {
                writeFilePort.addStudyRoomApplicationStatusExcelFile(
                    baseFile = file,
                    timeSlots = timeSlots,
                    studentSeatsMap = studentSeats.associateBy {
                        Pair(
                            Student.processGcn(it.studentGrade, it.studentClassRoom, it.studentNumber),
                            it.studentName
                        )
                    }
                )
            } ?: writeFilePort.writeStudyRoomApplicationStatusExcelFile(
                timeSlots = timeSlots,
                studentSeats = studentSeats
            ),
            fileName = getFileName(school.name)
        )
    }

    private fun getFileName(schoolName: String) =
        "${schoolName.replace(" ", "")}_자습실_신청상태_${LocalDateTime.now().format(File.FILE_DATE_FORMAT)}"
}