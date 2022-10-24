package team.aliens.dms.domain.meal.spi

import team.aliens.dms.domain.meal.model.Meal
import java.time.LocalDate
import java.util.UUID

interface QueryMealPort {
    fun queryAllByMealDateAndSchoolId(mealDate: LocalDate, schoolId: UUID): List<Meal>
}