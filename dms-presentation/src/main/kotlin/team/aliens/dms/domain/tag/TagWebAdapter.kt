package team.aliens.dms.domain.tag

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.tag.dto.QueryTagsResponse
import team.aliens.dms.domain.tag.usecase.CancelGrantedTagUseCase
import team.aliens.dms.domain.tag.usecase.QueryTagsUseCase
import java.util.UUID
import javax.validation.constraints.NotNull

@Validated
@RequestMapping("/tags")
@RestController
class TagWebAdapter(
    private val queryTagsUseCase: QueryTagsUseCase,
    private val cancelGrantedTagUseCase: CancelGrantedTagUseCase
) {

    @GetMapping
    fun queryTags(): QueryTagsResponse {
        return queryTagsUseCase.execute()
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/students")
    fun cancelGrantedTag(
        @RequestParam(name = "student_id") @NotNull studentId: UUID?,
        @RequestParam(name = "tag_id") @NotNull tagId: UUID?
    ) {
        cancelGrantedTagUseCase.execute(
            studentId = studentId!!,
            tagId = tagId!!
        )
    }
}
