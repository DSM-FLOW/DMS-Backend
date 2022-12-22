package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.exception.SeatNotFoundException
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort

@UseCase
class UnApplySeatUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandStudyRoomPort: CommandStudyRoomPort
) {

    fun execute() {
        val currentUserId = securityPort.getCurrentUserId()
        val seat = queryStudyRoomPort.querySeatByStudentId(currentUserId) ?: throw SeatNotFoundException

        commandStudyRoomPort.saveSeat(
            seat.copy(studentId = null)
        )
    }
}