package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.ModelStudent
import java.time.LocalDateTime

interface ModelStudentListPort {
    fun findModelStudents(startOfDay: LocalDateTime, endOfDay: LocalDateTime): List<ModelStudent>
}
