package team.aliens.dms.domain.vote

import jakarta.validation.Valid
import org.jetbrains.annotations.NotNull
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.vote.dto.reponse.GetAllVotingTopicResponse
import team.aliens.dms.domain.vote.dto.reponse.GetVotingTopicResponse
import team.aliens.dms.domain.vote.dto.request.CreateVoteTopicRequest
import team.aliens.dms.domain.vote.dto.request.CreateVotingTopicWebRequest
import team.aliens.dms.domain.vote.dto.request.UpdateVotingTopicRequest
import team.aliens.dms.domain.vote.dto.request.UpdateVotingTopicWebRequest
import team.aliens.dms.domain.vote.exception.NotValidPeriodException
import team.aliens.dms.domain.vote.usecase.CreateVotingTopicUseCase
import team.aliens.dms.domain.vote.usecase.DeleteVotingTopicUseCase
import team.aliens.dms.domain.vote.usecase.QueryAllVotingTopicUseCase
import team.aliens.dms.domain.vote.usecase.QueryVotingTopicUseCase
import team.aliens.dms.domain.vote.usecase.UpdateVotingTopicUseCase
import java.time.LocalDateTime


import java.util.UUID

@RestController
@RequestMapping("/votes")
class VoteWebAdapter(
    private val createVotingTopicUseCase: CreateVotingTopicUseCase,
    private val deleteVotingTopicUseCase: DeleteVotingTopicUseCase,
    private val queryVotingTopicUseCase: QueryVotingTopicUseCase,
    private val queryAllVotingTopicUseCase: QueryAllVotingTopicUseCase,
    private val updateVotingTopicUseCase: UpdateVotingTopicUseCase
) {
    @PostMapping
    fun saveVotingTopic(@RequestBody @Valid request: CreateVotingTopicWebRequest) {
        if (request.startTime!!.isBefore(LocalDateTime.now()) || request.endTime!!.isBefore(request.startTime)) {
            throw NotValidPeriodException
        }
        createVotingTopicUseCase.execute(
            CreateVoteTopicRequest(
                topicName = request.topicName!!,
                description = request.description!!,
                startTime = request.startTime!!,
                endTime = request.endTime!!,
                voteType = request.voteType!!
            )
        )
    }

    @PatchMapping
    fun updateVotingTopic(@RequestBody @Valid request: UpdateVotingTopicWebRequest) {
        if (request.startTime!!.isAfter(request.endTime) || request.endTime!!.isBefore(LocalDateTime.now())) {
            throw NotValidPeriodException
        }
        updateVotingTopicUseCase.execute(
            UpdateVotingTopicRequest(
                id = request.id!!,
                topicName = request.topicName!!,
                description = request.description!!,
                startTime = request.startTime!!,
                endTime = request.endTime!!,
                voteType = request.voteType!!
            )
        )
    }

    @DeleteMapping("/{voting-topic-id}")
    fun deleteVotingTopic(@PathVariable("voting-topic-id") @NotNull votingTopicId: UUID) {
        deleteVotingTopicUseCase.execute(votingTopicId)
    }

    @GetMapping("/{voting-topic-id}")
    fun getVotingTopic(@PathVariable("voting-topic-id") @NotNull votingTopicId: UUID): GetVotingTopicResponse {
        return queryVotingTopicUseCase.execute(votingTopicId)
    }

    @GetMapping
    fun getAllVotingTopic(): GetAllVotingTopicResponse {
        return queryAllVotingTopicUseCase.execute()
    }
}
