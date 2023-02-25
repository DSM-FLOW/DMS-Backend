package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.spi.StudentCommandRemainStatusPort
import team.aliens.dms.domain.student.spi.StudentCommandStudyRoomPort
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentQueryStudyRoomPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.time.LocalDateTime

@UseCase
class StudentWithdrawalUseCase(
    private val securityPort: StudentSecurityPort,
    private val queryUserPort: StudentQueryUserPort,
    private val commandRemainStatusPort: StudentCommandRemainStatusPort,
    private val queryStudyRoomPort: StudentQueryStudyRoomPort,
    private val commandStudyRoomPort: StudentCommandStudyRoomPort,
    private val commandUserPort: StudentCommandUserPort
) {

    fun execute() {
        val currentStudentId = securityPort.getCurrentUserId()
        val studentUser = queryUserPort.queryUserById(currentStudentId) ?: throw UserNotFoundException

        // 잔류 내역 삭제
        commandRemainStatusPort.deleteByStudentId(currentStudentId)

        // 자습실 신청 상태 제거
        queryStudyRoomPort.querySeatByStudentId(currentStudentId)?.let { seat ->
            val studyRoom = queryStudyRoomPort.queryStudyRoomById(seat.studyRoomId) ?: throw StudyRoomNotFoundException
            commandStudyRoomPort.saveSeat(
                seat.unUse()
            )
            commandStudyRoomPort.saveStudyRoom(
                studyRoom.unApply()
            )
        }

        commandUserPort.saveUser(
            studentUser.copy(deletedAt = LocalDateTime.now())
        )
    }
}