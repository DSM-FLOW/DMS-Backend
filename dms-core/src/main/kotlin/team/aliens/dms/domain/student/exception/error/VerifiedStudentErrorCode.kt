package team.aliens.dms.domain.student.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class VerifiedStudentErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    VERIFIED_STUDENT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Unverified Student", "STUDENT-404-2")
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = code
}
