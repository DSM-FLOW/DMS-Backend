package team.aliens.dms.persistence.auth.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed
import team.aliens.dms.domain.auth.model.EmailType
import javax.validation.constraints.NotNull

@RedisHash("tbl_authcode")
data class AuthCodeEntity(

    @Id
    val code: String,

    @Indexed
    @field:NotNull
    val email: String,

    @Indexed
    @field:NotNull
    val type: EmailType,

    @field:NotNull
    @TimeToLive
    val expirationTime: Int

)