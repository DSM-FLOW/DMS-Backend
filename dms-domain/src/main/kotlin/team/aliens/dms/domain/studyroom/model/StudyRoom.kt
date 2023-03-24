package team.aliens.dms.domain.studyroom.model

import java.util.UUID
import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.exception.StudyRoomAvailableGradeMismatchException
import team.aliens.dms.domain.studyroom.exception.StudyRoomAvailableSexMismatchException

@Aggregate
data class StudyRoom(

    val id: UUID = UUID(0, 0),

    val schoolId: UUID,

    val name: String,

    val floor: Int,

    val widthSize: Int,

    val heightSize: Int,

    val availableHeadcount: Int,

    val availableSex: Sex,

    val availableGrade: Int,

    val eastDescription: String,

    val westDescription: String,

    val southDescription: String,

    val northDescription: String

) {
    fun checkIsAvailableGradeAndSex(grade: Int, sex: Sex) {
        if (availableGrade != 0 && availableGrade != grade) {
            throw StudyRoomAvailableGradeMismatchException
        }

        if (availableSex != Sex.ALL && availableSex != sex) {
            throw StudyRoomAvailableSexMismatchException
        }
    }

    companion object {
        fun precessName(
            floor: Int,
            name: String,
            seatNameType: SeatNameType?
        ) = when (seatNameType) {
            SeatNameType.SHORT, null -> "${floor}-${name[0]}"
            SeatNameType.LONG -> "${floor}층 $name"
        }
    }
}
