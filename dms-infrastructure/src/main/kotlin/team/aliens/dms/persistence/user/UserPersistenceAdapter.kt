package team.aliens.dms.persistence.user

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.spi.UserPort
import team.aliens.dms.persistence.user.mapper.UserMapper
import team.aliens.dms.persistence.user.repository.UserJpaRepository
import java.util.UUID

@Component
class UserPersistenceAdapter(
    private val userMapper: UserMapper,
    private val userRepository: UserJpaRepository
) : UserPort {

    override fun existsUserByEmail(email: String) = userRepository.existsByEmail(email)

    override fun existsUserByAccountId(accountId: String): Boolean = userRepository.existsByAccountId(accountId)

    override fun saveUser(user: User) = userMapper.toDomain(
        userRepository.save(
            userMapper.toEntity(user)
        )
    )!!

    override fun queryUserById(userId: UUID) = userMapper.toDomain(
        userRepository.findByIdOrNull(userId)
    )

    override fun queryUserBySchoolId(schoolId: UUID) = userMapper.toDomain(
        userRepository.findBySchoolId(schoolId)
    )

    override fun queryUserByEmail(email: String) = userMapper.toDomain(
        userRepository.findByEmail(email)
    )

    override fun queryUserByAccountId(accountId: String) = userMapper.toDomain(
        userRepository.findByAccountId(accountId)
    )
}
 
