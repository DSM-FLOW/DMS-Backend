package team.aliens.dms.persistence.vote.entity

import jakarta.persistence.*
import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.manager.entity.ManagerJpaEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_voting_topic")
class VotingTopicJpaEntity(

    id: UUID?,

    @Column(name = "topic_name", columnDefinition = "VARCHAR(255)", nullable = false)
    val topicName: String,

    @Column(name = "description", columnDefinition = "VARCHAR(255)", nullable = false)
    val description: String,

    @Column(name = "start_time", columnDefinition = "DATETIME(6)", nullable = false)
    val startTime: LocalDateTime,

    @Column(name = "end_time", columnDefinition = "DATETIME(6)", nullable = false)
    val endTime: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(name = "vote_type", columnDefinition = "VARCHAR(20)", nullable = false)
    val voteType: VoteType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", columnDefinition = "BINARY(16)", nullable = false)
    val manager: ManagerJpaEntity,

) : BaseUUIDEntity(id)
