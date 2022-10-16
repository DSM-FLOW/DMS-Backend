package team.aliens.dms.domain.school.model

import team.aliens.dms.global.annotation.Aggregate
import java.time.LocalDate
import java.util.UUID

@Aggregate
data class School(

    val id: UUID,

    val name: String,

    val code: String,

    val question: String,

    val answer: String,

    val address: String,

    val contractStartedAt: LocalDate,

    val contractEndedAt: LocalDate
)