package team.aliens.dms.persistence.notification.entity

import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.user.entity.UserJpaEntity

@Entity
@Table(name = "tbl_user_notification")
class NotificationOfUserJpaEntity(

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)", nullable = false)
    val user: UserJpaEntity?,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val topic: Topic,

    @Column(columnDefinition = "VARCHAR(500)")
    val identifier: String?,

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    val title: String,

    @Column(columnDefinition = "VARCHAR(500)", nullable = false)
    val content: String,

    @Column(columnDefinition = "DATETIME(6)", nullable = false)
    val createdAt: LocalDateTime,

    @Column(columnDefinition = "BIT(1)", nullable = false)
    val isRead: Boolean

) : BaseUUIDEntity(id)
