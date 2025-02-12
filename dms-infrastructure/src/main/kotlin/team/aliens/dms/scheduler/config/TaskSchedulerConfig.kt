package team.aliens.dms.scheduler.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import javax.annotation.PreDestroy

@Configuration
@EnableScheduling
class TaskSchedulerConfig {

    private val scheduler = ThreadPoolTaskScheduler()
    @Bean
    fun taskScheduler(): ThreadPoolTaskScheduler {
        scheduler.poolSize = 5
        scheduler.setThreadNamePrefix("TeskScheduler-")
        scheduler.initialize()
        return scheduler
    }

    @PreDestroy
    fun shutdown() {
        scheduler.shutdown()
    }
}
