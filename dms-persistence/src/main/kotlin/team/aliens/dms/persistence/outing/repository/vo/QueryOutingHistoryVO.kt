package team.aliens.dms.persistence.outing.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.outing.spi.vo.OutingHistoryVO
import java.time.LocalTime
import java.util.UUID

class QueryOutingHistoryVO @QueryProjection constructor(
    outingApplicationId: UUID,
    studentName: String?,
    outingType: String,
    outingCompanionCount: Int,
    outingTime: LocalTime,
    arrivalTime: LocalTime
) : OutingHistoryVO(
    outingApplicationId = outingApplicationId,
    studentName = studentName,
    outingType = outingType,
    outingCompanionCount = outingCompanionCount,
    outingTime = outingTime,
    arrivalTime = arrivalTime
)
