package team.aliens.dms.domain.outing.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class OutingType(

    val outingType: String,

    val schoolId: UUID

)
