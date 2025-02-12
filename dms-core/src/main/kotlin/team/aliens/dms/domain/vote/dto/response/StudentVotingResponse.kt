package team.aliens.dms.domain.vote.dto.response

import java.util.UUID

data class StudentVotingResponse(
    val id: UUID,
    val name: String,
    val votes: Int,
    val classNumber: String
) {
    companion object {
        fun of(
            id: UUID,
            name: String,
            votes: Int,
            classNumber: String
        ) = StudentVotingResponse(
            id = id,
            name = name,
            votes = votes,
            classNumber = classNumber
        )
    }
}
