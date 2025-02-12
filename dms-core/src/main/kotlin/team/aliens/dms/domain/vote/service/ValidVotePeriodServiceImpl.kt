package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.exception.NotVotingPeriodException
import team.aliens.dms.domain.vote.spi.QueryVotingTopicPort
import java.time.LocalDateTime
import java.util.UUID

@Service
class ValidVotePeriodServiceImpl(
    val queryPort: QueryVotingTopicPort
) : ValidVotePeriodService {

    override fun excute(voteTopicId: UUID) {
        val startTime = queryPort.findStartTimeById(voteTopicId)
        val endTime = queryPort.findEndTimeById(voteTopicId)
        val currentTime = LocalDateTime.now()

        if (!(currentTime.isAfter(startTime) && currentTime.isBefore(endTime)))
            throw NotVotingPeriodException
    }
}
