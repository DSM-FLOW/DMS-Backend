package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointPort

@ReadOnlyUseCase
class QueryPointHistoryUseCase(
    private val securityPort: PointSecurityPort,
    private val queryPointPort: QueryPointPort
) {

    fun execute(type: PointRequestType): QueryPointHistoryResponse {
        val pointType = PointRequestType.toPointType(type)
        val currentStudentId = securityPort.getCurrentUserId()

        val pointHistory = if (pointType != null) {
            queryPointPort.queryPointHistoryByStudentIdAndType(currentStudentId, pointType)
        } else {
            queryPointPort.queryAllPointHistoryByStudentId(currentStudentId)
        }

        val totalPoint = pointHistory.sumOf {
            if (it.type == PointType.MINUS) {
                it.score * -1
            } else {
                it.score
            }
        }

        return QueryPointHistoryResponse(
            /**
             * BONUS -> 상점의 총합 ex) 상점 3점 = 3
             * MINUS -> 벌점의 총합 ex) 벌점 3점 = -3
             * ALL -> 상점 + 벌점의 총합
             **/
            totalPoint = totalPoint,
            points = pointHistory
        )
    }
}