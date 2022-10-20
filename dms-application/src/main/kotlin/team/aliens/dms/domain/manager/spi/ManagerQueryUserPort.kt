package team.aliens.dms.domain.manager.spi

import team.aliens.dms.domain.user.model.User
import java.util.UUID

interface ManagerQueryUserPort {
    fun queryUserById(id: UUID): User?

    fun queryUserBySchoolId(schoolId: UUID) : User?

    fun queryByAccountId(accountId: String): User?

    fun queryByEmail(email: String): User?
}