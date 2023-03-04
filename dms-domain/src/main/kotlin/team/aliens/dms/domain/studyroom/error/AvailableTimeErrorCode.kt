package team.aliens.dms.domain.studyroom.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class AvailableTimeErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    AVAILABLE_TIME_NOT_FOUND(ErrorStatus.NOT_FOUND, "Study Room Available Time Not Found")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
