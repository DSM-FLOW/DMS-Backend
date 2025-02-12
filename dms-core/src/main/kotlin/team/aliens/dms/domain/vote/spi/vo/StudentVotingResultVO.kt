package team.aliens.dms.domain.vote.spi.vo

import java.util.*

open class StudentVotingResultVO(
    val id: UUID,
    val name: String,
    val votes: Int
)
