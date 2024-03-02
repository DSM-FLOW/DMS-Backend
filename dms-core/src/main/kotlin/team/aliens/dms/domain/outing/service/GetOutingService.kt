package team.aliens.dms.domain.outing.service

import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.spi.vo.CurrentOutingApplicationVO
import team.aliens.dms.domain.outing.spi.vo.OutingApplicationVO
import java.time.LocalDate
import java.util.UUID

interface GetOutingService {

    fun getOutingType(outingType: OutingType): OutingType

    fun getAllOutingTypeTitlesBySchoolIdAndKeyword(schoolId: UUID, keyword: String?): List<String>

    fun getOutingApplicationById(outingApplicationId: UUID): OutingApplication

    fun getAllOutingApplicationVOsBetweenStartAndEnd(start: LocalDate, end: LocalDate): List<OutingApplicationVO>

    fun getCurrentOutingApplication(studentId: UUID): CurrentOutingApplicationVO
}
