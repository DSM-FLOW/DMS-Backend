package team.aliens.dms.persistence.user

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.user.spi.UserPort
import team.aliens.dms.persistence.user.mapper.UserMapper
import team.aliens.dms.persistence.user.repository.UserJpaRepository
import java.util.*

@Component
class UserPersistenceAdapter(
    private val userMapper: UserMapper
    private val userJpaRepository: UserJpaRepository
) : UserPort {

    override fun existsByEmail(email: String) = userJpaRepository.existsByEmail(email)
    
    override fun queryUserById(id: UUID) = userMapper.toDomain(
        userJpaRepository.findByIdOrNull(id)
    )
    
    override fun queryUserBySchoolId(schoolId: UUID) = userMapper.toDomain(
        userJpaRepository.findBySchoolId(schoolId)
    )
}
 
