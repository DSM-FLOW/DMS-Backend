package team.aliens.dms.domain.studyroom.dto

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.model.SeatStatus
import java.util.UUID

data class ManagerQueryStudyRoomResponse(
    val floor: Int,
    val name: String,
    val timeSlot: String?,
    val totalAvailableSeat: Int,
    val availableSex: Sex,
    val availableGrade: Int,
    val eastDescription: String,
    val westDescription: String,
    val southDescription: String,
    val northDescription: String,
    val totalWidthSize: Int,
    val totalHeightSize: Int,
    val seats: List<SeatElement>
) {

    data class SeatElement(
        val id: UUID,
        val widthLocation: Int,
        val heightLocation: Int,
        val number: Int?,
        val type: TypeElement?,
        val status: SeatStatus,
        val student: StudentElement?,
    ) {

        data class TypeElement(
            val id: UUID,
            val name: String,
            val color: String
        )

        data class StudentElement(
            val id: UUID,
            val gcn: String,
            val name: String,
            val profileImageUrl: String
        )
    }
}
