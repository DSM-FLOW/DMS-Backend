package team.aliens.dms.notice

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.notice.usecase.QueryNoticeDetailsUseCase
import team.aliens.dms.domain.notice.usecase.QueryNoticeStatusUseCase
import team.aliens.dms.notice.dto.response.GetNoticeStatusResponse
import team.aliens.dms.domain.notice.dto.QueryNoticeDetailsResponse
import java.util.UUID

@RequestMapping("/notices")
@RestController
class NoticeWebAdapter(
    private val queryNoticeStatusUseCase: QueryNoticeStatusUseCase,
    private val queryNoticeDetailsUseCase: QueryNoticeDetailsUseCase
) {

    @GetMapping("/status")
    fun getNoticeStatus(): GetNoticeStatusResponse {
        val result = queryNoticeStatusUseCase.execute()

        return GetNoticeStatusResponse(result)
    }

    @GetMapping("/{notice-id}")
    fun getDetails(@PathVariable("notice-id") noticeId: UUID): QueryNoticeDetailsResponse {
        return queryNoticeDetailsUseCase.execute(noticeId)
    }
}
