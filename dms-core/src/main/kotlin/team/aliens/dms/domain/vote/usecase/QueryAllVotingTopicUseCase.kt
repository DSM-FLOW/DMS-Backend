package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.vote.dto.reponse.GetAllVotingTopicResponse
import team.aliens.dms.domain.vote.service.GetVotingTopicService

@UseCase
class QueryAllVotingTopicUseCase(
    val getVotingTopicService: GetVotingTopicService
) {

    fun execute() = GetAllVotingTopicResponse.from(getVotingTopicService.getAllVotingTopics())
}
