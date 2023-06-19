package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.notification.dto.TopicSubscribeGroupsResponse
import team.aliens.dms.domain.notification.service.NotificationService

@ReadOnlyUseCase
class QueryTopicSubscribesUseCase(
    private val notificationService: NotificationService
) {

    fun execute(token: String): TopicSubscribeGroupsResponse {
        return TopicSubscribeGroupsResponse.of(
            notificationService.getTopicSubscribesByToken(token)
        )
    }
}
