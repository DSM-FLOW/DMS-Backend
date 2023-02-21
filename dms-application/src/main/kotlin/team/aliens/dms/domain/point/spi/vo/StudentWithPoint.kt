package team.aliens.dms.domain.point.spi.vo

import team.aliens.dms.domain.student.model.Student
import java.util.*

data class StudentWithPoint(
    val schoolId: UUID,
    val name: String,
    val grade: Int,
    val classRoom: Int,
    val number: Int,
    var bonusTotal: Int,
    var minusTotal: Int
) {
    val gcn: String = "${this.grade}${this.classRoom}${Student.processNumber(number)}"
}
