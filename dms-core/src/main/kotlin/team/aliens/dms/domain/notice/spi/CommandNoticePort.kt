package team.aliens.dms.domain.notice.spi

import team.aliens.dms.domain.notice.model.Notice
import java.time.LocalDateTime
import java.util.*

interface CommandNoticePort {

    fun deleteNotice(notice: Notice)
    fun scheduleVoteResultNoticeDelivery(endTime: LocalDateTime, managerId: UUID, title: String, content: String)
    fun saveNotice(notice: Notice): Notice
}
