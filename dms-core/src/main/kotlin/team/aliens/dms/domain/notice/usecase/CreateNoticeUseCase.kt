package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDateTime
import java.util.UUID

@UseCase
class CreateNoticeUseCase(
    private val userService: UserService,
    private val commentNoticePort: CommandNoticePort
) {

    fun execute(title: String, content: String): UUID {
        val user = userService.getCurrentUser()

        val notice = Notice(
            managerId = user.id,
            title = title,
            content = content,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val savedNotice = commentNoticePort.saveNotice(notice)

        return savedNotice.id
    }
}
