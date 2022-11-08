package team.aliens.dms.domain.auth.exception

import team.aliens.dms.domain.auth.error.AuthErrorCode
import team.aliens.dms.common.error.DmsException

object RefreshTokenNotFoundException : DmsException(
    AuthErrorCode.REFRESH_TOKEN_NOT_FOUND
)