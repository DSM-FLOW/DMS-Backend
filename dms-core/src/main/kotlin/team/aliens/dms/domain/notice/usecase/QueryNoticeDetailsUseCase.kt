package team.aliens.dms.domain.notice.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.notice.dto.QueryNoticeDetailsResponse
import team.aliens.dms.domain.notice.exception.NoticeNotFoundException
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.user.service.GetUserService
import team.aliens.dms.domain.user.spi.QueryUserPort

@ReadOnlyUseCase
class QueryNoticeDetailsUseCase(
    private val getUserService: GetUserService,
    private val queryNoticePort: QueryNoticePort,
    private val queryUserPort: QueryUserPort
) {

    fun execute(noticeId: UUID): QueryNoticeDetailsResponse {
        val user = getUserService.getCurrentUser()
        val notice = queryNoticePort.queryNoticeById(noticeId) ?: throw NoticeNotFoundException

        val writer = queryUserPort.queryUserById(notice.managerId) ?: throw ManagerNotFoundException

        validateSameSchool(writer.schoolId, user.schoolId)

        return QueryNoticeDetailsResponse(
            id = notice.id,
            title = notice.title,
            content = notice.content,
            createdAt = notice.createdAt!!
        )
    }
}
