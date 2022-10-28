package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.auth.spi.AuthQueryStudentPort
import team.aliens.dms.domain.meal.spi.MealQueryStudentPort
import team.aliens.dms.domain.user.spi.UserQueryStudentPort

interface StudentPort :
    QueryStudentPort,
    CommandStudentPort,
    AuthQueryStudentPort,
    MealQueryStudentPort,
    UserQueryStudentPort {
}