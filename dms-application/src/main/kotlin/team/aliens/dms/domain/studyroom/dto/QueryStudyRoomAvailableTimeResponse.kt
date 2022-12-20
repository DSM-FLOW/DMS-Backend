package team.aliens.dms.domain.studyroom.dto

import java.time.LocalTime

data class QueryStudyRoomAvailableTimeResponse(
    val startAt: LocalTime,
    val endAt: LocalTime
)
