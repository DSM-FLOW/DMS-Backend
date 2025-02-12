package team.aliens.dms.domain.vote.dto.request

import java.util.UUID


data class VoteResultNoticeRequest(
    val managerId: UUID,
    val title: String,
    val content: String,

)
