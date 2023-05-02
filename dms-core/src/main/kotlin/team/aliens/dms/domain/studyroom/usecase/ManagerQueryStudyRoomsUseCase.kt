package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.service.StudyRoomService
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@ReadOnlyUseCase
class ManagerQueryStudyRoomsUseCase(
    private val userService: UserService,
    private val studyRoomService: StudyRoomService
) {

    fun execute(timeSlotId: UUID): List<StudyRoomVO> {

        val user = userService.getCurrentUser()
        val timeSlot = studyRoomService.getTimeSlot(timeSlotId, user.schoolId)

        return studyRoomService.getStudyRoomVOs(timeSlot.id)
    }
}
