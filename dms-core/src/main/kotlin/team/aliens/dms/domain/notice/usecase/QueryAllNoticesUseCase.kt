package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.notice.dto.QueryAllNoticesResponse
import team.aliens.dms.domain.notice.dto.QueryAllNoticesResponse.NoticeDetails
import team.aliens.dms.domain.notice.model.OrderType
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryAllNoticesUseCase(
    private val userService: UserService,
    private val queryNoticePort: QueryNoticePort
) {

    fun execute(orderType: String): QueryAllNoticesResponse {
        val user = userService.getCurrentUser()
        val order = OrderType.valueOf(orderType)
        val notices = queryNoticePort.queryAllNoticesBySchoolIdAndOrder(user.schoolId, order)

        return QueryAllNoticesResponse(
            notices.map {
                NoticeDetails(
                    id = it.id,
                    title = it.title,
                    createdAt = it.createdAt!!
                )
            }
        )
    }
}
