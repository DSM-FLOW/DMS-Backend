package team.aliens.dms.domain.vote

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.vote.dto.request.CreateVotingOptionRequest
import team.aliens.dms.domain.vote.dto.request.CreateVotingOptionWebRequest
import team.aliens.dms.domain.vote.dto.response.VotingOptionsResponse
import team.aliens.dms.domain.vote.usecase.CreateVoteUseCase
import team.aliens.dms.domain.vote.usecase.CreateVotingOptionUseCase
import team.aliens.dms.domain.vote.usecase.QueryVotesUseCase
import team.aliens.dms.domain.vote.usecase.QueryVotingOptionsUseCase
import team.aliens.dms.domain.vote.usecase.RemoveVoteUseCase
import team.aliens.dms.domain.vote.usecase.RemoveVotingOptionUseCase
import java.util.UUID

@RestController
@RequestMapping("/vote")
class VoteWebAdapter(
    private val createVoteUseCase: CreateVoteUseCase,
    private val createVotingOptionUseCase: CreateVotingOptionUseCase,
    private val queryVotesUseCase: QueryVotesUseCase,
    private val queryVotingOptionsUseCase: QueryVotingOptionsUseCase,
    private val removeVoteUseCase: RemoveVoteUseCase,
    private val removeVotingOptionUseCase: RemoveVotingOptionUseCase
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/option")
    fun createVotingOption(@RequestBody request: CreateVotingOptionWebRequest) {
        createVotingOptionUseCase.execute(CreateVotingOptionRequest.of(
                request.votingTopicId,
                request.optionName
        ))
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/option/{votingTopicId}")
    fun getVotingOptions(@PathVariable votingTopicId: UUID): List<VotingOptionsResponse> {
        return queryVotingOptionsUseCase.execute(votingTopicId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/option/{votingOptionId}")
    fun removeVotingOption(@PathVariable votingOptionId: UUID) {
        removeVotingOptionUseCase.execute(votingOptionId)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/student/{votingTopicId}")
    fun createVote(
        @PathVariable votingTopicId: UUID,
        @RequestParam(name = "selected_id") selectedId: UUID
    ) {
        createVoteUseCase.execute(selectedId, votingTopicId)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/student/{votingTopicId}")
    fun removeVote(@PathVariable votingTopicId: UUID) {
        removeVoteUseCase.execute(votingTopicId)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/result/{votingTopicId}")
    fun getVoteResults(@PathVariable votingTopicId: UUID): Any {
        return queryVotesUseCase.execute(votingTopicId)
    }
}
