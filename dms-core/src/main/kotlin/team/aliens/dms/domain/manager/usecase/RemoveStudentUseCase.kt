package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.service.RemainService
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDateTime
import java.util.UUID

@UseCase
class RemoveStudentUseCase(
    private val userService: UserService,
    private val queryStudentPort: QueryStudentPort,
    private val remainService: RemainService,
    private val commandStudyRoomPort: CommandStudyRoomPort,
    private val commandStudentPort: CommandStudentPort
) {

    fun execute(studentId: UUID) {

        val user = userService.getCurrentUser()
        val student = queryStudentPort.queryStudentById(studentId) ?: throw StudentNotFoundException
        validateSameSchool(student.schoolId, user.schoolId)

        remainService.deleteRemainStatusByStudentId(studentId)
        commandStudyRoomPort.deleteSeatApplicationByStudentId(studentId)

        commandStudentPort.saveStudent(
            student.copy(deletedAt = LocalDateTime.now())
        )

        student.userId?.let {
            userService.deleteUserById(it)
        }
    }
}
