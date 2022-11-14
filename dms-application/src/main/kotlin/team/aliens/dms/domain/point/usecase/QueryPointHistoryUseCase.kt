package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.spi.PointQueryUserPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointPort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@ReadOnlyUseCase
class QueryPointHistoryUseCase(
    private val queryPointPort: QueryPointPort,
    private val queryUserPort: PointQueryUserPort,
    private val securityPort: PointSecurityPort
) {

    fun execute(type: PointRequestType): QueryPointHistoryResponse {
        val pointType = PointRequestType.toPointType(type)

        val currentUserId = securityPort.getCurrentUserId()
        val student = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val pointHistory = pointType?.let {
            queryPointPort.queryPointHistoryByStudentIdAndType(student.id, pointType)
        } ?: queryPointPort.queryAllPointHistoryByStudentId(student.id)

        val points = pointHistory.map {
            QueryPointHistoryResponse.Point(
                pointId = it.pointId,
                date = it.date,
                type = it.type,
                name = it.name,
                score = it.score
            )
        }

        return QueryPointHistoryResponse(
            totalPoint = points.sumOf { it.score },
            points = points
        )
    }
}