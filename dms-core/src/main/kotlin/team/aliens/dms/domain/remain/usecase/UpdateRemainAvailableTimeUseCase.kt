package team.aliens.dms.domain.remain.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.remain.dto.UpdateRemainAvailableTimeRequest
import team.aliens.dms.domain.remain.model.RemainAvailableTime
import team.aliens.dms.domain.remain.spi.CommandRemainAvailableTimePort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort

@UseCase
class UpdateRemainAvailableTimeUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val commandRemainAvailableTimePort: CommandRemainAvailableTimePort
) {

    fun execute(request: UpdateRemainAvailableTimeRequest) {
        val currentUserId = securityPort.getCurrentUserId()
        val currentUser = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        commandRemainAvailableTimePort.saveRemainAvailableTime(
            RemainAvailableTime(
                id = currentUser.schoolId,
                startDayOfWeek = request.startDayOfWeek,
                startTime = request.startTime,
                endDayOfWeek = request.endDayOfWeek,
                endTime = request.endTime
            )
        )
    }
}
