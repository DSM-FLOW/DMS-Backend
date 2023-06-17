package team.aliens.dms.domain.notification.model

import java.time.LocalDateTime
import java.util.UUID
import team.aliens.dms.domain.notice.model.Notice

sealed class Notification(

    val schoolId: UUID,

    val topic: Topic,

    val identifier: String?,

    val title: String,

    val content: String,

    val threadId: String,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    private val isSaveRequired: Boolean

) {
    fun toUserNotification(userId: UUID): UserNotification =
        UserNotification(
            userId = userId,
            topic = topic,
            identifier = identifier,
            title = title,
            content = content,
            createdAt = createdAt
        )

    fun runIfSaveRequired(function: () -> Unit) {
        if (isSaveRequired) function.invoke()
    }

    class NoticeNotification(
        schoolId: UUID,
        notice: Notice
    ) : Notification(
        schoolId = schoolId,
        topic = Topic.NOTICE,
        identifier = notice.id.toString(),
        title = "${notice.title}",
        content = "기숙사 공지가 등록되었습니다.",
        threadId = notice.id.toString(),
        isSaveRequired = true
    )
}
