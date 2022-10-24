package team.aliens.dms.domain.meal.dto

import java.time.LocalDate

data class MealDetailsResponse(
    val meals: List<Meal>
) {

    data class Meal(
        val date: LocalDate,
        val breakfast: List<String>,
        val lunch: List<String>,
        val dinner: List<String>
    )
}