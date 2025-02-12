package team.aliens.dms.domain.vote.dto.response

class OptionVotingResponse(
    val name: String,
    val votes: Int
) {
    companion object {
        fun of(
            name: String,
            votes: Int,
        ) = OptionVotingResponse(
            name = name,
            votes = votes,
        )
    }
}
