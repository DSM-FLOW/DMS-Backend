package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.remain.dto.QueryRemainAvailableTimeResponse
import team.aliens.dms.domain.remain.exception.RemainAvailableTimeNotFoundException
import team.aliens.dms.domain.remain.spi.QueryRemainAvailableTimePort
import team.aliens.dms.domain.user.service.GetUserService

@ReadOnlyUseCase
class QueryRemainAvailableTimeUseCase(
    private val getUserService: GetUserService,
    private val queryRemainAvailableTimePort: QueryRemainAvailableTimePort
) {

    fun execute(): QueryRemainAvailableTimeResponse {

        val user = getUserService.getCurrentUser()

        val availableTime = queryRemainAvailableTimePort.queryRemainAvailableTimeBySchoolId(user.schoolId)
            ?: throw RemainAvailableTimeNotFoundException

        return QueryRemainAvailableTimeResponse(
            startDayOfWeek = availableTime.startDayOfWeek,
            startTime = availableTime.startTime,
            endDayOfWeek = availableTime.endDayOfWeek,
            endTime = availableTime.endTime
        )
    }
}
