package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.QueryCurrentAppliedStudyRoomResponse
import team.aliens.dms.domain.studyroom.exception.AppliedSeatNotFound
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort

@ReadOnlyUseCase
class QueryCurrentAppliedStudyRoomUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryStudyRoomPort: QueryStudyRoomPort
) {

    fun execute(): QueryCurrentAppliedStudyRoomResponse {
        val currentUserId = securityPort.getCurrentUserId()

        val seat = queryStudyRoomPort.querySeatByStudentId(currentUserId) ?: throw AppliedSeatNotFound
        val studyRoom = queryStudyRoomPort.queryStudyRoomById(seat.studyRoomId) ?: throw StudyRoomNotFoundException

        return QueryCurrentAppliedStudyRoomResponse(
            floor = studyRoom.floor,
            name = studyRoom.name
        )
    }
}