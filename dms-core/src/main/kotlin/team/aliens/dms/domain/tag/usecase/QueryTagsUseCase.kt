package team.aliens.dms.domain.tag.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.tag.dto.QueryTagsResponse
import team.aliens.dms.domain.tag.dto.TagResponse
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryTagsUseCase(
    private val queryTagPort: QueryTagPort,
    private val userService: UserService
) {

    fun execute(): QueryTagsResponse {

        val user = userService.getCurrentUser()
        val tags = queryTagPort.queryTagsBySchoolId(user.schoolId)
            .map {
                TagResponse(
                    id = it.id,
                    name = it.name,
                    color = it.color
                )
            }

        return QueryTagsResponse(tags)
    }
}
