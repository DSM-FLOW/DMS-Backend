package team.aliens.dms.domain.vote.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.vote.exception.error.VotingTopicErrorCode

object NotValidPeriodException : DmsException(
    VotingTopicErrorCode.NOT_VALID_PERIOD
)
