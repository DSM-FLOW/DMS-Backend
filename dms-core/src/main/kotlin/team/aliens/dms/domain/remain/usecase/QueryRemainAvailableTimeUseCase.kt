package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.remain.dto.QueryRemainAvailableTimeResponse
import team.aliens.dms.domain.remain.exception.RemainAvailableTimeNotFoundException
import team.aliens.dms.domain.remain.spi.QueryRemainAvailableTimePort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort

@ReadOnlyUseCase
class QueryRemainAvailableTimeUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val queryRemainAvailableTimePort: QueryRemainAvailableTimePort
) {

    fun execute(): QueryRemainAvailableTimeResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val currentUser = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val availableTime = queryRemainAvailableTimePort.queryRemainAvailableTimeBySchoolId(currentUser.schoolId)
            ?: throw RemainAvailableTimeNotFoundException

        return QueryRemainAvailableTimeResponse(
            startDayOfWeek = availableTime.startDayOfWeek,
            startTime = availableTime.startTime,
            endDayOfWeek = availableTime.endDayOfWeek,
            endTime = availableTime.endTime
        )
    }
}
