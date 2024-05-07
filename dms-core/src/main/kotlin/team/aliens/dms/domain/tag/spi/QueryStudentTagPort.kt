package team.aliens.dms.domain.tag.spi

import team.aliens.dms.domain.tag.model.StudentTag
import java.util.UUID

interface QueryStudentTagPort {

    fun queryStudentTagsByTagNameIn(names: List<String>): List<StudentTag>

    fun queryStudentTagsByStudentId(studentId: UUID): List<StudentTag>
}
