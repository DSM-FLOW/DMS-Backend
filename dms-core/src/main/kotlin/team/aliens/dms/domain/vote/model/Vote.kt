package team.aliens.dms.domain.vote.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class Vote(
    val id: UUID = UUID(0, 0),
    val votingTopicId: UUID,
    val studentId: UUID,
    val votedAt: LocalDateTime = LocalDateTime.now(),
    val selectedOptionId: UUID?,
    val selectedStudentId: UUID?
)
