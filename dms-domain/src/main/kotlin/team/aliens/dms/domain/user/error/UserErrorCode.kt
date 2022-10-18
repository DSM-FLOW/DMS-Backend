package team.aliens.dms.domain.user.error

import team.aliens.dms.global.error.ErrorProperty

enum class UserErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    USER_NOT_FOUND(404, "User Not Found"),
    STUDENT_EMAIL_EXISTS(409, "Student Email Exists")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}