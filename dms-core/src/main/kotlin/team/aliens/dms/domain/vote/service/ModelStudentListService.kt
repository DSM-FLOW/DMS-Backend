package team.aliens.dms.domain.vote.service

import team.aliens.dms.domain.vote.dto.response.ModelStudentListResponse
import java.time.LocalDate

interface ModelStudentListService {
    fun getModelStudentList(date: LocalDate): List<ModelStudentListResponse>
}
