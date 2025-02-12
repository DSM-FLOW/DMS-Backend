package team.aliens.dms.domain.vote.dto.reponse

import team.aliens.dms.domain.vote.model.VotingTopic

class GetAllVotingTopicResponse(
    val votingTopics: List<VotingTopic?>
) {
    companion object {
        fun from(votingTopics: List<VotingTopic?>): GetAllVotingTopicResponse {
            return GetAllVotingTopicResponse(
                votingTopics
            )
        }
    }
}
