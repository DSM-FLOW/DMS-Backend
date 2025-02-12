package team.aliens.dms.domain.vote.spi.vo

import java.util.UUID

open class OptionVotingResultVO(
    val id: UUID,
    val name: String,
    val votes: Int
)
