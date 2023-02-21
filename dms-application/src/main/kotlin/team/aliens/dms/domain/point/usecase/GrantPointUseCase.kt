package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.point.dto.GrantPointRequest
import team.aliens.dms.domain.point.exception.PointOptionNotFoundException
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.spi.*
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import java.time.LocalDateTime

@UseCase
class GrantPointUseCase(
    private val queryManagerPort: PointQueryManagerPort,
    private val securityPort: PointSecurityPort,
    private val queryPointOptionPort: QueryPointOptionPort,
    private val commandPointHistoryPort: CommandPointHistoryPort,
    private val queryStudentPort: PointQueryStudentPort
) {

    fun execute(request: GrantPointRequest) {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryManagerPort.queryManagerById(currentUserId) ?: throw ManagerNotFoundException

        val pointOption =
            queryPointOptionPort.queryPointOptionById(request.pointOptionId) ?: throw PointOptionNotFoundException
        pointOption.checkSchoolId(manager.schoolId)

        val students =
            queryStudentPort.queryStudentsWithPointHistory(request.studentIdList)

        if(students.size != request.studentIdList.size) {
            throw StudentNotFoundException
        }

        val pointHistories = students
            .map {
                val (updatedBonusTotal, updatedMinusTotal) = PointHistory.getUpdatedTotalPoint(
                    pointOption.score, pointOption.type, it.bonusTotal, it.minusTotal
                )

                PointHistory(
                    studentName = it.name,
                    studentGcn = it.gcn,
                    bonusTotal = updatedBonusTotal,
                    minusTotal = updatedMinusTotal,
                    isCancel = false,
                    pointName = pointOption.name,
                    pointScore = pointOption.score,
                    pointType = pointOption.type,
                    createdAt = LocalDateTime.now(),
                    schoolId = manager.schoolId
                )
            }


        commandPointHistoryPort.saveAllPointHistories(pointHistories)
    }
}