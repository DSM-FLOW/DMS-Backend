package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.point.exception.PointOptionNotFoundException
import team.aliens.dms.domain.point.spi.CommandPointOptionPort
import team.aliens.dms.domain.point.spi.QueryPointOptionPort
import team.aliens.dms.domain.user.service.GetUserService
import java.util.UUID

@UseCase
class RemovePointOptionUseCase(
    private val getUserService: GetUserService,
    private val queryPointOptionPort: QueryPointOptionPort,
    private val commandPointOptionPort: CommandPointOptionPort
) {

    fun execute(pointOptionId: UUID) {

        val user = getUserService.getCurrentUser()

        val pointOption = queryPointOptionPort.queryPointOptionById(pointOptionId) ?: throw PointOptionNotFoundException
        pointOption.checkSchoolId(user.schoolId)

        commandPointOptionPort.deletePointOption(pointOption)
    }
}
