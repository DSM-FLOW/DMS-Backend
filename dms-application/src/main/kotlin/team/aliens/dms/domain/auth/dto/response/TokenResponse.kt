package team.aliens.dms.domain.auth.dto.response

import java.util.Date

data class TokenResponse(

    val accessToken: String,

    val expiredAt: Date,

    val refreshToken: String
)
