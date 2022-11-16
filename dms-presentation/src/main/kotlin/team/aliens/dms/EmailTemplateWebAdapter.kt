package team.aliens.dms

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.TemplateUseCase

@RequestMapping("/templates")
@RestController
class EmailTemplateWebAdapter(
    private val templateUseCase: TemplateUseCase
) {

    @PostMapping
    fun createTemplate(@RequestBody request: TemplateRequest) {
        templateUseCase.create(request.type)
    }

    @PatchMapping
    fun updateTemplate(@RequestBody request: TemplateRequest) {
        templateUseCase.update(request.type)
    }

    @DeleteMapping
    fun deleteTemplate(@RequestBody request: TemplateRequest) {
        templateUseCase.delete(request.type)
    }
}

data class TemplateRequest(
    val type: String
)