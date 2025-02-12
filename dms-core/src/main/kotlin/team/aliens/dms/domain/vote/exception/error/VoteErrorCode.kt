package team.aliens.dms.domain.vote.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class VoteErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    VOTE_TYPE_MISMATCH(ErrorStatus.BAD_REQUEST, "Vote Type mismatch", 1),
    VOTING_TOPIC_NAME_ALREADY_EXIST(ErrorStatus.CONFLICT, "Voting Topic Already Exist", 1),
    VOTING_TOPIC_NOT_FOUND(ErrorStatus.NOT_FOUND, "Couldn't found voting topic", 1),
    WRONG_VOTE_TYPE(ErrorStatus.BAD_REQUEST, "Wrong vote type", 1),
    VOTING_OPTION_NOT_FOUND(ErrorStatus.NOT_FOUND, "Couldn't found voting option", 1),
    VOTE_NOT_FOUND(ErrorStatus.NOT_FOUND, "Couldn't found vote", 1),
    ALREADY_VOTED(ErrorStatus.CONFLICT, "Already voted", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "VOTE-$status-$sequence"
}
