package team.aliens.dms.domain.remain.service

import team.aliens.dms.domain.remain.model.RemainAvailableTime

interface CheckAccessibleRemainAvailableTime {

    fun execute(availableTime: RemainAvailableTime): Boolean

}