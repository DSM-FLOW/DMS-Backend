package team.aliens.dms.persistence.vote.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import team.aliens.dms.persistence.BaseEntity
import java.util.UUID

@Entity
@Table(name = "tbl_voting_option")
class VotingOptionJpaEntity(

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voting_topic_id", columnDefinition = "BINARY(16)", nullable = false)
    val votingTopic: VotingTopicJpaEntity?,
    @Column(columnDefinition = "VARCHAR(255)")
    val optionName: String

) : BaseEntity(id)
