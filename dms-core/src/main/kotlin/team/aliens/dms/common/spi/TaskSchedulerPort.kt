package team.aliens.dms.common.spi

import java.time.LocalDateTime
import java.util.*

interface TaskSchedulerPort {
    fun schduleTask(id: UUID, task: Runnable, time: LocalDateTime)
    fun cancelTask(id: UUID)
}
