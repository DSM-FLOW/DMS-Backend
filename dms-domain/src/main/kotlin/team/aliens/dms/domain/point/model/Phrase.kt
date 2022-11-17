package team.aliens.dms.domain.point.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class Phrase(

    val id: UUID = UUID(0, 0),

    val content: String,

    val type: PointType,

    val standard: Int

)