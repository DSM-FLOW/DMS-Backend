package team.aliens.dms.domain.vote.service

import team.aliens.dms.domain.vote.dto.request.VoteResultNoticeRequest
import java.time.LocalDateTime
import java.util.UUID

interface ScheduleCreateVoteResultNoticeService {

    fun execute(id: UUID, reservedTime: LocalDateTime, voteResultNoticeRequest: VoteResultNoticeRequest, schoolId: UUID)
}
