package team.aliens.dms.domain.point.spi

import java.util.UUID

interface QueryPointOptionPort {
    fun queryPointOptionById(pointOptionId: UUID): PointOption?

    fun queryPointOptionsBySchoolIdAndKeyword(schoolId: UUID, keyword: String?): List<PointOption>
    fun existByNameAndSchoolId(name: String, schoolId: UUID): Boolean
}
    fun queryPointOptionById(pointOptionId: UUID): PointOption?