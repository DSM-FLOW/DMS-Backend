package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.file.model.File
import team.aliens.dms.domain.file.spi.WriteFilePort
import team.aliens.dms.domain.point.dto.ExportAllPointHistoryResponse
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.spi.PointQueryUserPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.time.LocalDateTime

@ReadOnlyUseCase
class ExportAllPointHistoryUseCase(
    private val securityPort: PointSecurityPort,
    private val queryUserPort: PointQueryUserPort,
    private val queryPointHistoryPort: QueryPointHistoryPort,
    private val writeFilePort: WriteFilePort
) {

    fun execute(start: LocalDateTime?, end: LocalDateTime?): ExportAllPointHistoryResponse {

        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val pointHistories = queryPointHistoryPort.queryPointHistoryBySchoolIdAndCreatedAtBetween(
            schoolId = manager.schoolId,
            startAt = start,
            endAt = end
        )

        return ExportAllPointHistoryResponse(
            file = writeFilePort.writePointHistoryExcelFile(pointHistories),
            fileName = getFileName(start, end, pointHistories)
        )
    }

    private fun getFileName(
        start: LocalDateTime?,
        end: LocalDateTime?,
        pointHistories: List<PointHistory>
    ): String {
        val startDateString = (start ?: pointHistories.last().createdAt)
            .format(File.FILE_DATE_FORMAT)

        val endDateString = (end ?: LocalDateTime.now())
            .format(File.FILE_DATE_FORMAT)

        return "상벌점_부여내역_${startDateString}_${endDateString}"
    }
}