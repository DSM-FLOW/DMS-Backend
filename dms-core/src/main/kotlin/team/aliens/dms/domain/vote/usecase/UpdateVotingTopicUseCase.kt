package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.common.spi.TaskSchedulerPort
import team.aliens.dms.domain.vote.dto.request.UpdateVotingTopicRequest
import team.aliens.dms.domain.vote.dto.request.VoteResultNoticeRequest
import team.aliens.dms.domain.vote.exception.NotValidPeriodException
import team.aliens.dms.domain.vote.exception.VotingAlreadyEndedException
import team.aliens.dms.domain.vote.service.CommendVotingTopicService
import team.aliens.dms.domain.vote.service.GetVotingTopicService
import team.aliens.dms.domain.vote.service.ScheduleCreateVoteResultNoticeService
import java.time.LocalDateTime

@UseCase
class UpdateVotingTopicUseCase(
    private val taskSchedulerPort: TaskSchedulerPort,
    private val commandVotingTopicService: CommendVotingTopicService,
    private val getVotingTopicService: GetVotingTopicService,
    private val scheduleCreateVoteResultNoticeService: ScheduleCreateVoteResultNoticeService,
    private val securityPort: SecurityPort
) {

    fun execute(request: UpdateVotingTopicRequest) {

        val votingTopic = getVotingTopicService.getVotingTopicById(request.id)
        if (votingTopic.endTime.isBefore(LocalDateTime.now())) {
            throw VotingAlreadyEndedException
        }

        val userId = securityPort.getCurrentUserId()
        val schoolId = securityPort.getCurrentUserSchoolId()

        val savedVotingTopicId = commandVotingTopicService.saveVotingTopic(
            votingTopic.copy(
                topicName = request.topicName,
                voteType = request.voteType,
                description = request.description,
                startTime = request.startTime,
                endTime = request.endTime
            )
        )

        taskSchedulerPort.cancelTask(request.id)

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
