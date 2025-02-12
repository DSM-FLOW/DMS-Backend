package team.aliens.dms.global.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.stereotype.Component
import team.aliens.dms.common.annotation.DomainService
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.annotation.SchedulerUseCase
import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.annotation.UseCase

@Configuration
@Component
@ComponentScan(
    basePackages = ["team.aliens.dms"],
    includeFilters = [
        Filter(
            type = FilterType.ANNOTATION,
            classes = [
                UseCase::class,
                ReadOnlyUseCase::class,
                SchedulerUseCase::class,
                DomainService::class,
                Service::class

            ]
        )
    ]
)
class ComponentScanConfig
