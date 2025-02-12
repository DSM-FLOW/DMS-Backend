package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notice.dto.NoticeIdResponse
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.service.NoticeService
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDateTime

@UseCase
class CreateNoticeUseCase(
    private val userService: UserService,
    private val noticeService: NoticeService,
) {

    fun execute(title: String, content: String): NoticeIdResponse {
        val user = userService.getCurrentUser()

        val notice = Notice(
            managerId = user.id,
            title = title,
            content = content,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val savedNotice = noticeService.saveNotice(notice)

        return NoticeIdResponse(savedNotice.id)
    }
}
