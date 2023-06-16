package team.aliens.dms.persistence.notification

import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.persistence.notification.mapper.DeviceTokenMapper
import team.aliens.dms.persistence.notification.repository.DeviceTokenJpaRepository
import java.util.UUID

@Component
class DeviceTokenPersistenceAdapter(
    private val notificationMapper: DeviceTokenMapper,
    private val deviceTokenRepository: DeviceTokenJpaRepository
) : DeviceTokenPort {

    override fun saveDeviceToken(deviceToken: DeviceToken) = notificationMapper.toDomain(
        deviceTokenRepository.save(
            notificationMapper.toEntity(deviceToken)
        )
    )!!

    override fun queryDeviceTokenByUserId(userId: UUID) = notificationMapper.toDomain(
        deviceTokenRepository.queryByUserId(userId)
    )

    override fun queryDeviceTokenByDeviceToken(deviceToken: String) = notificationMapper.toDomain(
        deviceTokenRepository.queryByDeviceToken(deviceToken)
    )

    override fun deleteDeviceTokenByUserId(userId: UUID) {
        deviceTokenRepository.deleteByUserId(userId)
    }
}
