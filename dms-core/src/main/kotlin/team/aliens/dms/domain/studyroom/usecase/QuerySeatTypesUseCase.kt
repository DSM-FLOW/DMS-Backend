package team.aliens.dms.domain.studyroom.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.studyroom.dto.QuerySeatTypesResponse
import team.aliens.dms.domain.studyroom.dto.QuerySeatTypesResponse.TypeElement
import team.aliens.dms.domain.studyroom.spi.QuerySeatTypePort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.util.UUID

@ReadOnlyUseCase
class QuerySeatTypesUseCase(
    private val securityPort: StudyRoomSecurityPort,
    private val queryUserPort: StudyRoomQueryUserPort,
    private val querySeatTypePort: QuerySeatTypePort
) {

    fun execute(studyRoomId: UUID?): QuerySeatTypesResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val seatTypes = studyRoomId?.let {
            querySeatTypePort.queryAllSeatTypeByStudyRoomId(studyRoomId)
        } ?: querySeatTypePort.queryAllSeatTypeBySchoolId(user.schoolId)

        return QuerySeatTypesResponse(
            seatTypes.map {
                TypeElement(
                    id = it.id,
                    name = it.name,
                    color = it.color
                )
            }
        )
    }
}
