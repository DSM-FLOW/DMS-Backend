package team.aliens.dms.domain.studyroom.error

import team.aliens.dms.common.error.ErrorProperty

enum class StudyRoomErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    STUDY_ROOM_NOT_FOUND(404, "Study Room Not Found")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}