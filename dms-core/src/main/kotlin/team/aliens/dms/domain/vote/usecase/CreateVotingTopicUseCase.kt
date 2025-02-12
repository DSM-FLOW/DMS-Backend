package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.vote.dto.request.CreateVoteTopicRequest
import team.aliens.dms.domain.vote.dto.request.VoteResultNoticeRequest
import team.aliens.dms.domain.vote.exception.NotValidPeriodException
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.service.CommendVotingTopicService
import team.aliens.dms.domain.vote.service.ScheduleCreateVoteResultNoticeService
import java.time.LocalDateTime

@UseCase
class CreateVotingTopicUseCase(
    private val commendVotingTopicService: CommendVotingTopicService,
    private val scheduleCreateVoteResultNoticeService: ScheduleCreateVoteResultNoticeService,
    private val securityPort: SecurityPort
) {

    fun execute(request: CreateVoteTopicRequest) {

        val userId = securityPort.getCurrentUserId()
        val schoolId = securityPort.getCurrentUserSchoolId()

        val savedVotingTopicId = commendVotingTopicService.saveVotingTopic(
            VotingTopic(
                managerId = userId,
                topicName = request.topicName,
                description = request.description,
                startTime = request.startTime,
                endTime = request.endTime,
                voteType = request.voteType,
            )
        )

        scheduleCreateVoteResultNoticeService.execute(
            savedVotingTopicId,
            request.endTime,
            VoteResultNoticeRequest(
                userId,
                "ex)모범학생 투표 결과",
                "쿼리 메서드로 이렇게 들어감 ex)1 학년 : 홍길동,임꺽정,유씨, 2학년 ...."
            ),
            schoolId
        )
    }
}
