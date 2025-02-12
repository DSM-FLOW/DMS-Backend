package team.aliens.dms.domain.vote.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.vote.exception.error.VotingTopicErrorCode

object NotFoundVotingTopicException : DmsException(
    VotingTopicErrorCode.NOT_FOUND_VOTING_TOPIC
)
