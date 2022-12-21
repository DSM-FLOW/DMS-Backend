package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.QuerySeatTypesResponse
import team.aliens.dms.domain.studyroom.dto.QuerySeatTypesResponse.TypeElement
import team.aliens.dms.domain.studyroom.spi.QuerySeatTypePort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort

@ReadOnlyUseCase
class QuerySeatTypesUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val querySeatTypePort: QuerySeatTypePort
) {

    fun execute(): QuerySeatTypesResponse {
        val currentUserId = securityPort.getCurrentUserId()

        val seatTypes = querySeatTypePort.queryAllSeatTypeByUserId(currentUserId).map {
            TypeElement(
                id = it.id,
                name = it.name,
                color = it.color
            )
        }

        return QuerySeatTypesResponse(seatTypes)
    }
}