package team.aliens.dms.domain.student.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class StudentErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    SEX_MISMATCH(ErrorStatus.BAD_REQUEST, "Sex Mismatch", "STUDENT-400-1"),

    STUDENT_INFO_MISMATCH(ErrorStatus.UNAUTHORIZED, "Student Info Mismatch", "STUDENT-401-1"),

    STUDENT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Student Not Found", "STUDENT-404-1"),

    STUDENT_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Student Already Exists", "STUDENT-409-1")
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = code
}
