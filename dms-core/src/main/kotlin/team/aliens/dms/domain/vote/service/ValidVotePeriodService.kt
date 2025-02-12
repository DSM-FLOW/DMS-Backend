package team.aliens.dms.domain.vote.service

import java.util.UUID

interface ValidVotePeriodService {

    fun excute(voteTopicId: UUID)
}
