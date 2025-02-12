package team.aliens.dms.persistence.vote.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.student.entity.StudentJpaEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_vote")
class VoteJpaEntity(

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voting_topic_id", columnDefinition = "BINARY(16)", nullable = false)
    val votingTopic: VotingTopicJpaEntity?,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", columnDefinition = "BINARY(16)", nullable = false)
    val student: StudentJpaEntity?,
    @Column(columnDefinition = "DATETIME")
    val votedAt: LocalDateTime,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id", columnDefinition = "BINARY(16)", nullable = true)
    val selectedOption: VotingOptionJpaEntity?,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_student_id", columnDefinition = "BINARY(16)", nullable = true)
    val selectedStudent: StudentJpaEntity?
) : BaseEntity(id)
