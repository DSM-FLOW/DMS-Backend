package team.aliens.dms.domain.tag.spi

import java.util.UUID

interface TagSecurityPort {
    fun getCurrentUserId(): UUID
}