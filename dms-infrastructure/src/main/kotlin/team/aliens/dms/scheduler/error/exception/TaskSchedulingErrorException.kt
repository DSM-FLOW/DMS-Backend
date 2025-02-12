package team.aliens.dms.scheduler.error.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.scheduler.error.SchdulerErrorCode

object TaskSchedulingErrorException : DmsException(
    SchdulerErrorCode.TASK_SCHEDULING_ERROR
)