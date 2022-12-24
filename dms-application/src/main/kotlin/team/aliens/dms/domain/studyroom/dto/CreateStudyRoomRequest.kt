package team.aliens.dms.domain.studyroom.dto

import java.util.UUID

data class CreateStudyRoomRequest(
    val floor: Int,
    val name: String,
    val totalWidthSize: Int,
    val totalHeightSize: Int,
    val eastDescription: String,
    val westDescription: String,
    val southDescription: String,
    val northDescription: String,
    val availableSex: String,
    val availableGrade: Int,
    val seats : List<SeatRequest>
) {
    data class SeatRequest(
        val widthLocation: Int,
        val heightLocation: Int,
        val number: Int?,
        val typeId: UUID?,
        val status: String
    )
}
