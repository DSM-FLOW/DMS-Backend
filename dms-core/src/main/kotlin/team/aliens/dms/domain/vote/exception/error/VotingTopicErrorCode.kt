package team.aliens.dms.domain.vote.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class VotingTopicErrorCode(
    val status: Int,
    val message: String,
    val sequence: Int
) : ErrorProperty {
    NOT_VALID_PERIOD(ErrorStatus.BAD_REQUEST, "Not Valid Period", 1),
    NOT_FOUND_VOTING_TOPIC(ErrorStatus.BAD_REQUEST, "Not Found VotingTopic", 2),
    NOT_VOTING_PERIOD(ErrorStatus.BAD_REQUEST, "Not Voting Period", 3),
    VOTING_AREADY_ENDED(ErrorStatus.BAD_REQUEST, "Voting Aready Ended", 4);


    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "VOTE-$status-$sequence"
}
