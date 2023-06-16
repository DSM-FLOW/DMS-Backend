package team.aliens.dms.persistence.notification.entity

import java.io.Serializable
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.Table
import team.aliens.dms.domain.notification.model.Topic

@Entity
@Table(name = "tbl_topic_subscribe")
class TopicSubscribeJpaEntity(

    @EmbeddedId
    val id: TopicSubscribeJpaEntityId,

    @MapsId("deviceTokenId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_token_id", columnDefinition = "BINARY(16)", nullable = false)
    val deviceToken: DeviceTokenJpaEntity

)

@Embeddable
data class TopicSubscribeJpaEntityId(

    @Column
    val deviceTokenId: UUID,

    @MapsId("topic")
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val topic: Topic

) : Serializable
