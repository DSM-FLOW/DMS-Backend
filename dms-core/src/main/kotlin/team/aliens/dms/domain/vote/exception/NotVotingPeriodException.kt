package team.aliens.dms.domain.vote.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.vote.exception.error.VotingTopicErrorCode

object NotVotingPeriodException : DmsException(
    VotingTopicErrorCode.NOT_VOTING_PERIOD
)
