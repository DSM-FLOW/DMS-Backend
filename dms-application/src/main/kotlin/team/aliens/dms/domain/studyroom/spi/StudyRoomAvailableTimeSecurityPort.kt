package team.aliens.dms.domain.studyroom.spi

import java.util.UUID

interface StudyRoomAvailableTimeSecurityPort {

    fun getCurrentUserId(): UUID

}