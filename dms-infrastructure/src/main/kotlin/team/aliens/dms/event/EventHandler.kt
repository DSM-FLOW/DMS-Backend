package team.aliens.dms.event

import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.aliens.dms.domain.notification.service.NotificationService

@Component
class NotificationEventHandler(
    private val notificationService: NotificationService
) {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onNotificationEvent(event: TopicNotificationEvent) {

        when (event) {
            is SingleNotificationEvent -> {
                notificationService.sendMessage(
                    token = event.token,
                    notification = event.notification
                )
            }
            is GroupNotificationEvent -> {
                notificationService.sendMessages(
                    deviceTokens = event.tokens,
                    notification = event.notification
                )
            }
            else -> {
                notificationService.sendMessagesByTopic(
                    notification = event.notification
                )
            }
        }
    }
}
