package team.aliens.dms.domain.vote.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.vote.exception.error.VoteErrorCode

object VoteTypeMismatchException : DmsException(
    VoteErrorCode.VOTE_TYPE_MISMATCH
)
object VotingTopicAlreadyExistException : DmsException(
    VoteErrorCode.VOTING_TOPIC_NAME_ALREADY_EXIST
)
object VotingTopicNotFoundException : DmsException(
    VoteErrorCode.VOTING_TOPIC_NOT_FOUND
)
object VotingOptionNotFoundException : DmsException(
    VoteErrorCode.VOTING_OPTION_NOT_FOUND
)
object WrongVoteTypeException : DmsException(
    VoteErrorCode.WRONG_VOTE_TYPE
)
object VoteNotFoundException : DmsException(
    VoteErrorCode.VOTE_NOT_FOUND
)
object AlreadyVotedException : DmsException(
    VoteErrorCode.ALREADY_VOTED
)
