package team.aliens.dms.domain.file.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.file.error.FileErrorCode

object ExelValueDuplicateException : DmsException(
    FileErrorCode.EXEL_VALUE_DUPLICATE
)