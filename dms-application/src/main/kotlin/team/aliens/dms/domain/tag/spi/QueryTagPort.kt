package team.aliens.dms.domain.tag.spi

import team.aliens.dms.domain.tag.model.Tag
import java.util.UUID

interface QueryTagPort {

    fun queryTagsBySchoolId(schoolId: UUID): List<Tag>

    fun existsByNameAndSchoolId(name: String, schoolId: UUID): Boolean
}
