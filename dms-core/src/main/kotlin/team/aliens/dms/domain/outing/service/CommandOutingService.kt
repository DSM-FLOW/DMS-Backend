package team.aliens.dms.domain.outing.service

import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingType

interface CommandOutingService {

    fun apply(outingApplication: OutingApplication): OutingApplication

    fun saveOutingType(outingType: OutingType): OutingType
}
