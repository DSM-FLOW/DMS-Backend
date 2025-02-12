package team.aliens.dms.scheduler.error


import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class SchdulerErrorCode(
    val status: Int,
    val message: String,
    val squence: Int
) : ErrorProperty {

    TASK_SCHEDULING_ERROR(ErrorStatus.INTERNAL_SERVER_ERROR, "Task Schduling Error", 1);

    override fun status() = status
    override fun message() = message
    override fun code() = "SCHEDULER-$status-$squence"

}