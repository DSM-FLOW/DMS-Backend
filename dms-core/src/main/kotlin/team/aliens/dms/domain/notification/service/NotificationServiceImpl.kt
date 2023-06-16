package team.aliens.dms.domain.notification.service

import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.dto.TopicSubscribeResponse
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.model.TopicSubscribe
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.domain.notification.spi.NotificationPort
import team.aliens.dms.domain.notification.spi.TopicSubscribePort
import java.util.UUID

@Component
class NotificationServiceImpl(
    private val notificationPort: NotificationPort,
    private val topicSubscribePort: TopicSubscribePort,
    private val deviceTokenPort: DeviceTokenPort
) : NotificationService {

    override fun saveDeviceToken(deviceToken: DeviceToken) {
        deviceTokenPort.saveDeviceToken(deviceToken)
        notificationPort.subscribeAllTopics(
            deviceToken = deviceToken.deviceToken
        )
    }

    override fun subscribeTopic(deviceToken: String, topic: Topic) {
        val savedToken = this.getDeviceTokenByDeviceToken(deviceToken)
        topicSubscribePort.saveTopicSubscribe(
            TopicSubscribe(
                deviceTokenId = savedToken.id,
                topic = topic
            )
        )
        notificationPort.subscribeTopic(
            deviceToken = this.getDeviceTokenByDeviceToken(deviceToken).deviceToken,
            topic = topic
        )
    }

    override fun unsubscribeTopic(deviceToken: String, topic: Topic) {
        notificationPort.unsubscribeTopic(
            deviceToken = deviceToken,
            topic = topic
        )
    }

    override fun updateSubscribes(deviceToken: String, topicsToSubscribe: List<Pair<Topic, Boolean>>) {

        val savedToken = this.getDeviceTokenByDeviceToken(deviceToken)

        val subscribes = mutableListOf<Topic>()
        val unsubscribes = mutableListOf<Topic>()

        topicsToSubscribe.forEach { (topic, isSubscribe) ->
            if (isSubscribe) {
                notificationPort.subscribeTopic(
                    deviceToken = deviceToken,
                    topic = topic
                )
                subscribes.add(topic)
            } else {
                notificationPort.unsubscribeTopic(
                    deviceToken = deviceToken,
                    topic = topic
                )
                unsubscribes.add(topic)
            }
        }

        topicSubscribePort.saveAllTopicSubscribes(
            subscribes.map { TopicSubscribe(savedToken.id, it) }
        )
        topicSubscribePort.deleteByUserIdAndTopics(
            userId = savedToken.id,
            topics = unsubscribes
        )
    }

    private fun getDeviceTokenByDeviceToken(deviceToken: String) =
        deviceTokenPort.queryDeviceTokenByDeviceToken(deviceToken) ?: throw DeviceTokenNotFoundException

    override fun sendMessage(deviceToken: String, notification: Notification) {
        notificationPort.sendMessage(
            deviceToken = deviceToken,
            notification = notification
        )
    }

    override fun sendMessages(deviceTokens: List<String>, notification: Notification) {
        notificationPort.sendMessages(
            deviceTokens = deviceTokens,
            notification = notification
        )
    }

    override fun sendMessagesByTopic(notification: Notification) {
        notificationPort.sendByTopic(
            notification = notification
        )
    }

    override fun getTopicSubscribesByDeviceToken(deviceToken: String): List<TopicSubscribeResponse> {
        val savedToken = getDeviceTokenByDeviceToken(deviceToken)
        return topicSubscribePort.queryTopicSubscribesByDeviceTokenId(savedToken.id).map {
            TopicSubscribeResponse(
                topic = it.topic,
                isSubscribed = true
            )
        }.toMutableList().also {
            Topic.values().forEach { topic ->
                if (!it.any { it.topic == topic }) {
                    it.add(
                        TopicSubscribeResponse(
                            topic = topic,
                            isSubscribed = false
                        )
                    )
                }
            }
        }
    }
}
