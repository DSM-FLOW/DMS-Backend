package team.aliens.dms.domain.tag.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.tag.model.StudentTag
import team.aliens.dms.domain.tag.model.Tag
import team.aliens.dms.domain.tag.spi.CommandStudentTagPort
import team.aliens.dms.domain.tag.spi.CommandTagPort
import java.util.UUID

@Service
class CommandTagServiceImpl(
    private val commandStudentTagPort: CommandStudentTagPort,
    private val commandTagPort: CommandTagPort
) : CommandTagService {

    override fun deleteAllStudentTagsByTagIdInOrStudentIdIn(tagIds: List<UUID>, studentIds: List<UUID>) {
        commandStudentTagPort.deleteAllStudentTagsByTagIdInOrStudentIdIn(tagIds, studentIds)
    }

    override fun deleteStudentTagById(studentId: UUID, tagId: UUID) {
        commandStudentTagPort.deleteStudentTagById(studentId, tagId)
    }

    override fun deleteStudentTagAndTagById(tagId: UUID) {
        commandStudentTagPort.deleteStudentTagByTagId(tagId)
        commandTagPort.deleteTagById(tagId)
    }

    override fun saveTag(tag: Tag): Tag {
        return commandTagPort.saveTag(tag)
    }

    override fun saveStudentTag(studentTag: StudentTag): StudentTag =
        commandStudentTagPort.saveStudentTag(studentTag)

    override fun saveAllStudentTags(studentTags: List<StudentTag>) {
        commandStudentTagPort.saveAllStudentTags(studentTags)
    }
}
