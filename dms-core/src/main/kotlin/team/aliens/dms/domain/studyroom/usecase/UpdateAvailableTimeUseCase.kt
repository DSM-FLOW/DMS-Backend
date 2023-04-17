package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.studyroom.model.AvailableTime
import team.aliens.dms.domain.studyroom.spi.CommandAvailableTimePort
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalTime

@UseCase
class UpdateAvailableTimeUseCase(
    private val userService: UserService,
    private val commandAvailableTimePort: CommandAvailableTimePort,
) {

    fun execute(startAt: LocalTime, endAt: LocalTime) {

        val user = userService.getCurrentUser()

        commandAvailableTimePort.saveAvailableTime(
            AvailableTime(
                schoolId = user.schoolId,
                startAt = startAt,
                endAt = endAt
            )
        )
    }
}
