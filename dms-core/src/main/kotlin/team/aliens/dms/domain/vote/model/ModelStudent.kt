package team.aliens.dms.domain.vote.model

import java.util.UUID

data class ModelStudent(
    val id: UUID,
    val studentGcn: String,
    val studentName: String,
    val studentProfile: String?

)
