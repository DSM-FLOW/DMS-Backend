package team.aliens.dms.domain.meal.spi

import team.aliens.dms.domain.student.model.Student
import java.util.UUID

interface MealQueryStudentPort {
    fun queryByUserId(userId: UUID): Student?
}