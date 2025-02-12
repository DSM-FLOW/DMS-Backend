package team.aliens.dms.domain.vote

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.vote.dto.response.ModelStudentListResponse
import team.aliens.dms.domain.vote.usecase.GetModelStudentsUseCase
import java.time.LocalDate

@RestController
@RequestMapping("/votes")
class VoteWebAdapter(
    private val getModelStudentsUseCase: GetModelStudentsUseCase
) {
    @GetMapping("/candidate-list/{date}")
    fun getModelStudents(
        @PathVariable
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        date: LocalDate
    ): List<ModelStudentListResponse> {
        return getModelStudentsUseCase.execute(date)
    }
}
