package team.aliens.dms.domain.user.exception

import team.aliens.dms.domain.user.error.UserErrorCode
import team.aliens.dms.global.error.DmsException

object UserNotFoundException : DmsException(
    UserErrorCode.USER_NOT_FOUND
)