package team.aliens.dms.domain.school.aop

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.school.exception.FeatureNotAvailableException
import team.aliens.dms.domain.school.exception.FeatureNotFoundException
import team.aliens.dms.domain.school.model.AvailableFeature
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.user.service.GetUserService

@UseCase
@Aspect
class AvailableFeatureAspect(
    private val getUserService: GetUserService,
    private val querySchoolPort: QuerySchoolPort
) {

    @Before(
        "within(team.aliens.dms.domain.meal.usecase.*) && " +
            "!@target(team.aliens.dms.common.annotation.SchedulerUseCase)"
    )
    fun beforeMealService() {
        val availableFeature = getAvailableFeature()
        if (!availableFeature.mealService) {
            throw FeatureNotAvailableException
        }
    }

    @Before(
        "within(team.aliens.dms.domain.notice.usecase.*) && " +
            "!@target(team.aliens.dms.common.annotation.SchedulerUseCase)"
    )
    fun beforeNoticeService() {
        val availableFeature = getAvailableFeature()
        if (!availableFeature.noticeService) {
            throw FeatureNotAvailableException
        }
    }

    @Before(
        "within(team.aliens.dms.domain.point.usecase.*) && " +
            "!@target(team.aliens.dms.common.annotation.SchedulerUseCase)"
    )
    fun beforePointService() {
        val availableFeature = getAvailableFeature()
        if (!availableFeature.pointService) {
            throw FeatureNotAvailableException
        }
    }

    @Before(
        "within(team.aliens.dms.domain.studyroom.usecase.*) && " +
            "!@target(team.aliens.dms.common.annotation.SchedulerUseCase)"
    )
    fun beforeStudyRoomService() {
        val availableFeature = getAvailableFeature()
        if (!availableFeature.studyRoomService) {
            throw FeatureNotAvailableException
        }
    }

    @Before(
        "within(team.aliens.dms.domain.remain.usecase.*) && " +
            "!@annotation(team.aliens.dms.common.annotation.SchedulerUseCase)"
    )
    fun beforeRemainRoomService() {
        val availableFeature = getAvailableFeature()
        if (!availableFeature.remainService) {
            throw FeatureNotAvailableException
        }
    }

    private fun getAvailableFeature(): AvailableFeature {
        val user = getUserService.getCurrentUser()
        return querySchoolPort.queryAvailableFeaturesBySchoolId(user.schoolId) ?: throw FeatureNotFoundException
    }
}
