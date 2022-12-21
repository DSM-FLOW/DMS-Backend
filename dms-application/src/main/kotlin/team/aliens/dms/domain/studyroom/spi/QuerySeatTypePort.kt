package team.aliens.dms.domain.studyroom.spi

import java.util.UUID
import team.aliens.dms.domain.studyroom.model.SeatType

interface QuerySeatTypePort {

    fun querySeatTypeByUserId(userId: UUID): List<SeatType>

}