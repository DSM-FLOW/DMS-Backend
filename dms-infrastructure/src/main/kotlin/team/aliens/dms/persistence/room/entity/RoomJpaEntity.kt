package team.aliens.dms.persistence.room.entity

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import team.aliens.dms.persistence.BaseUUIDEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity

@Entity
@Table(name = "tbl_room",
    uniqueConstraints = [
        UniqueConstraint(columnNames = arrayOf("school_id", "number"))
    ]
)
class RoomJpaEntity(

    override val id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(columnDefinition = "VARCHAR(4)", nullable = false, unique = true)
    val number: String

) : BaseUUIDEntity(id)
