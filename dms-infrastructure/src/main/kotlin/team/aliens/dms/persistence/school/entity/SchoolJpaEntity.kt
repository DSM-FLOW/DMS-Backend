package team.aliens.dms.persistence.school.entity

import team.aliens.dms.persistence.BaseUUIDEntity
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "tbl_school",
    uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("name", "address"))
    ]
)
class SchoolJpaEntity(

    override val id: UUID,

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    val name: String,

    @Column(columnDefinition = "CHAR(8)", nullable = false, unique = true)
    val code: String,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    val question: String,

    @Column(columnDefinition = "VARCHAR(100)", nullable = false)
    val answer: String,

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    val address: String,

    @Column(columnDefinition = "DATE", nullable = false)
    val contractStartedAt: LocalDate,

    @Column(columnDefinition = "DATE")
    val contractEndedAt: LocalDate?

) : BaseUUIDEntity()