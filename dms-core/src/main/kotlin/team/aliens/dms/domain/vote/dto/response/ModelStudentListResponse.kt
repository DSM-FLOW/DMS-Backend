package team.aliens.dms.domain.vote.dto.response

import java.util.UUID

data class ModelStudentListResponse(
    val id: UUID,
    val gcn: String,
    val name: String,
    val profileImageUrl: String?
)