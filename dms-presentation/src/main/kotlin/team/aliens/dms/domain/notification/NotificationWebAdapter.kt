package team.aliens.dms.domain.notification

import javax.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.notification.dto.SetDeviceTokenRequest
import team.aliens.dms.domain.notification.dto.request.UpdateTopicSubscribesWebRequest
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.usecase.SetDeviceTokenUseCase
import team.aliens.dms.domain.notification.usecase.SubscribeTopicUseCase
import team.aliens.dms.domain.notification.usecase.UnsubscribeTopicUseCase
import team.aliens.dms.domain.notification.usecase.UpdateTopicSubscribesUseCase

@Validated
@RequestMapping("/notifications")
@RestController
class NotificationWebAdapter(
    private val setDeviceTokenUseCase: SetDeviceTokenUseCase,
    private val subscribeTopicUseCase: SubscribeTopicUseCase,
    private val unsubscribeTopicUseCase: UnsubscribeTopicUseCase,
    private val updateTopicSubscribesUseCase: UpdateTopicSubscribesUseCase
) {

    @PostMapping("/token")
    fun setDeviceToken(@RequestParam("token") @NotNull deviceToken: String) {
        setDeviceTokenUseCase.execute(
            SetDeviceTokenRequest(deviceToken = deviceToken)
        )
    }

    @PostMapping("/topic")
    fun subscribeTopic(@RequestParam @NotNull topic: Topic) {
        subscribeTopicUseCase.execute(topic)
    }

    @DeleteMapping("/topic")
    fun unsubscribeTopic(@RequestParam @NotNull topic: Topic) {
        unsubscribeTopicUseCase.execute(topic)
    }

    @PatchMapping("/topic")
    fun updateTopicSubscribes(@RequestParam @NotNull request: UpdateTopicSubscribesWebRequest) {
        updateTopicSubscribesUseCase.execute(
            topicsToSubscribe = request.topicsToSubscribe.map{it.toPair()}
        )
    }
}
