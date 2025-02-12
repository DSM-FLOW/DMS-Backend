package team.aliens.dms.domain.vote.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import team.aliens.dms.domain.vote.model.VoteType
import java.time.LocalDateTime
import java.util.UUID

data class UpdateVotingTopicWebRequest(

    @field:NotNull
    val id: UUID,

    @field:NotBlank
    val topicName: String?,

    @field:NotBlank
    val description: String?,

    @NotNull
    val voteType: VoteType?,

    @field:NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val startTime: LocalDateTime?,

    @field:NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val endTime: LocalDateTime?,

)
