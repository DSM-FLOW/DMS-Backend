package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.tag.exception.TagNotFoundException
import team.aliens.dms.domain.tag.spi.CommandTagPort
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.tag.spi.TagQueryUserPort
import team.aliens.dms.domain.tag.spi.TagSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.util.UUID

@UseCase
class UpdateTagUseCase(
    private val securityPort: TagSecurityPort,
    private val queryUserPort: TagQueryUserPort,
    private val commandTagPort: CommandTagPort,
    private val queryTagPort: QueryTagPort
) {

    fun execute(tagId: UUID, newName: String, newColor: String) {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException
        
        val tag = queryTagPort.queryTagById(tagId) ?: throw TagNotFoundException
        validateSameSchool(tag.schoolId, manager.schoolId)

        commandTagPort.saveTag(
            tag.copy(
                name = newName,
                color = newColor
            )
        )
    }
}
