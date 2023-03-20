package team.aliens.dms.domain.studyroom.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.studyroom.error.StudyRoomErrorCode

object TimeSlotAlreadyExistException : DmsException(
    StudyRoomErrorCode.STUDY_ROOM_NOT_FOUND
)