package team.aliens.dms.domain.vote.service

import java.time.LocalDateTime
import java.util.UUID

interface ScheduleCreateVoteResultNoticeService {
    fun exectue(endTime:LocalDateTime, managerId:UUID, schoolId:UUID, title:String, content:String)
}