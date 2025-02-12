package team.aliens.dms.persistence.vote.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.vote.spi.vo.StudentVotingResultVO
import java.util.*

open class QueryStudentVotingResultVO @QueryProjection constructor(
    id: UUID,
    name: String,
    votes: Int
) : StudentVotingResultVO(
    id = id,
    name = name,
    votes = votes
)
