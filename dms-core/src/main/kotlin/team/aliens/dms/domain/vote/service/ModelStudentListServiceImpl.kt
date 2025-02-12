package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.dto.response.ModelStudentListResponse
import team.aliens.dms.domain.vote.spi.ModelStudentListPort
import java.time.LocalDate

@Service
class ModelStudentListServiceImpl(
    private val modelStudentListPort: ModelStudentListPort
) : ModelStudentListService {
    override fun getModelStudentList(date: LocalDate): List<ModelStudentListResponse> {

        val firstDayOfMonth = date.withDayOfMonth(1)
        val lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth())

        val startOfDay = firstDayOfMonth.atStartOfDay()
        val endOfDay = lastDayOfMonth.atTime(23, 59, 59)

        val modelStudentList = modelStudentListPort.findModelStudents(startOfDay, endOfDay)

        if (modelStudentList.isEmpty()) {
            throw NoSuchElementException("학생을 찾을 수 없습니다")
        }

        return modelStudentList.map { student ->
            ModelStudentListResponse(
                id = student.id,
                gcn = student.studentGcn,
                name = student.studentName,
                profileImageUrl = student.studentProfile
            )
        }
    }
}
