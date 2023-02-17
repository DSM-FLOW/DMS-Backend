package team.aliens.dms.persistence.remain.entity

import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "tbl_remain_available_time")
class RemainAvailableTimeJpaEntity(

    @Id
    val id: UUID,

    @MapsId("schoolId")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(columnDefinition = "TINYINT", nullable = false)
    val startDayOfWeek: DayOfWeek,

    @Column(columnDefinition = "TIME", nullable = false)
    val startTime: LocalTime,

    @Column(columnDefinition = "TINYINT", nullable = false)
    val endDayOfWeek: DayOfWeek,

    @Column(columnDefinition = "TIME", nullable = false)
    val endTime: LocalTime

)