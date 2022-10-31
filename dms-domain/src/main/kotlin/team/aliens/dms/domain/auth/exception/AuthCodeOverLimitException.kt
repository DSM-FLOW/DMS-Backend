package team.aliens.dms.domain.auth.exception

import team.aliens.dms.domain.auth.error.AuthErrorCode
import team.aliens.dms.common.error.DmsException

object AuthCodeOverLimitException : DmsException(
    AuthErrorCode.AUTH_CODE_OVER_LIMITED
)