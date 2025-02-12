package team.aliens.dms.persistence.vote.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.vote.spi.vo.OptionVotingResultVO
import java.util.UUID

class QueryOptionVotingResult @QueryProjection constructor(
    id: UUID,
    name: String,
    votes: Int
) : OptionVotingResultVO(
    id = id,
    name = name,
    votes = votes
)
