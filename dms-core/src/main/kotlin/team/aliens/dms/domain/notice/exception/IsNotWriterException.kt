package team.aliens.dms.domain.notice.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.notice.exception.error.NoticeErrorCode

object IsNotWriterException : DmsException(
    NoticeErrorCode.IS_NOT_WRITER
)
