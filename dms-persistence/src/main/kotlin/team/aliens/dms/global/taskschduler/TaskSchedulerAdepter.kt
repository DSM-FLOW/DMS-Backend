package team.aliens.dms.global.taskschduler

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component
import team.aliens.dms.common.spi.TaskSchedulerPort
import java.time.LocalDateTime
import java.time.ZoneId

@Component
class TaskSchedulerAdepter(
    private val taskScheduler: ThreadPoolTaskScheduler
): TaskSchedulerPort {

    override fun schduleTask(task: Runnable, time: LocalDateTime) {
        val InstantTime = time.atZone(ZoneId.systemDefault()).toInstant()
        taskScheduler.schedule(task, InstantTime)
    }

}