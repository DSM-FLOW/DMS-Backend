package team.aliens.dms.domain.tag.spi

import team.aliens.dms.domain.tag.model.StudentTag
import java.util.UUID

interface CommandStudentTagPort {

    fun deleteAllStudentTagsByTagIdInOrStudentIdIn(tagIds: List<UUID>, studentIds: List<UUID>)

    fun deleteStudentTagById(studentId: UUID, tagId: UUID)

    fun deleteStudentTagByTagId(tagId: UUID)

    fun saveStudentTag(studentTag: StudentTag): StudentTag

    fun saveAllStudentTags(studentTags: List<StudentTag>)
}
