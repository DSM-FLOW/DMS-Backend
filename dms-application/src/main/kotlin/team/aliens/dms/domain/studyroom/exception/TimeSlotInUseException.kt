package team.aliens.dms.domain.studyroom.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.studyroom.error.StudyRoomErrorCode

object TimeSlotInUseException : DmsException(
    StudyRoomErrorCode.TIME_SLOT_IN_USE
)
