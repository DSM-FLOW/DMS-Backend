package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.remain.dto.QueryRemainAvailableTimeResponse
import team.aliens.dms.domain.remain.exception.RemainAvailableTimeCanNotAccessException
import team.aliens.dms.domain.remain.exception.RemainAvailableTimeNotFoundException
import team.aliens.dms.domain.remain.service.CheckAccessibleRemainAvailableTime
import team.aliens.dms.domain.remain.spi.QueryRemainAvailableTimePort
import team.aliens.dms.domain.remain.spi.RemainQueryUserPort
import team.aliens.dms.domain.remain.spi.RemainSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@ReadOnlyUseCase
class QueryRemainAvailableTimeUseCase(
    private val securityPort: RemainSecurityPort,
    private val queryUserPort: RemainQueryUserPort,
    private val queryRemainAvailableTimePort: QueryRemainAvailableTimePort,
    private val checkAccessibleRemainAvailableTime: CheckAccessibleRemainAvailableTime
) {

    fun execute(): QueryRemainAvailableTimeResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val currentUser = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val availableTime = queryRemainAvailableTimePort.queryRemainAvailableTimeBySchoolId(currentUser.schoolId)
            ?: throw RemainAvailableTimeNotFoundException

        if (!checkAccessibleRemainAvailableTime.execute(availableTime)) {
            throw RemainAvailableTimeCanNotAccessException
        }

        return QueryRemainAvailableTimeResponse(
            startDayOfWeek = availableTime.startDayOfWeek,
            startTime = availableTime.startTime,
            endDayOfWeek = availableTime.endDayOfWeek,
            endTime = availableTime.endTime
        )
    }
}