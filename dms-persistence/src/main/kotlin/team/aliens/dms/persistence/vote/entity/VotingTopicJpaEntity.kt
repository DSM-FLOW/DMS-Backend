package team.aliens.dms.persistence.vote.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.manager.entity.ManagerJpaEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_voting_topic")
class VotingTopicJpaEntity(

    id: UUID?,

    @Column(nullable = false)
    val topicName: String,
    @Column(columnDefinition = "DATETIME")
    val startTime: LocalDateTime?,
    @Column(columnDefinition = "DATETIME")
    val endTime: LocalDateTime?,
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val voteType: VoteType,
    @ManyToOne
    @JoinColumn(name = "manager_id", columnDefinition = "BINARY(16)", nullable = false)
    val manager: ManagerJpaEntity?

) : BaseEntity(id)
