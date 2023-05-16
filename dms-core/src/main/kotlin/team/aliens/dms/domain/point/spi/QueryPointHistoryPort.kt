package team.aliens.dms.domain.point.spi

import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.vo.AllPointHistoryVO
import team.aliens.dms.domain.point.spi.vo.PointHistoryVO
import java.time.LocalDateTime
import java.util.UUID

interface QueryPointHistoryPort {

    fun queryPointHistoryById(pointHistoryId: UUID): PointHistory?

    fun queryBonusAndMinusTotalPointByStudentGcnAndName(
        gcn: String,
        studentName: String
    ): Pair<Int, Int>

    fun queryPointHistoryByStudentGcnAndNameAndType(
        gcn: String,
        studentName: String,
        type: PointType? = null,
        isCancel: Boolean? = null,
        pageData: PageData = PageData.DEFAULT
    ): List<PointHistoryVO>

    fun queryPointHistoryBySchoolIdAndType(
        schoolId: UUID,
        type: PointType?,
        isCancel: Boolean? = null,
        pageData: PageData = PageData.DEFAULT
    ): List<AllPointHistoryVO>

    fun queryPointHistoryBySchoolIdAndCreatedAtBetween(
        schoolId: UUID,
        startAt: LocalDateTime?,
        endAt: LocalDateTime?
    ): List<PointHistory>
}
