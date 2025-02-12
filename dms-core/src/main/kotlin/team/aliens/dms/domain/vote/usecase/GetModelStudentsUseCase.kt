package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.vote.dto.response.ModelStudentListResponse
import team.aliens.dms.domain.vote.service.ModelStudentListService
import java.time.LocalDate

@ReadOnlyUseCase
class GetModelStudentsUseCase(
    private val modelStudentListService: ModelStudentListService
) {
    fun execute(date: LocalDate): List<ModelStudentListResponse> {
        return modelStudentListService.getModelStudentList(date)
    }
}
