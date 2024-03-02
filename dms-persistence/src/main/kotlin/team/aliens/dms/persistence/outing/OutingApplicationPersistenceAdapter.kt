package team.aliens.dms.persistence.outing

import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.domain.outing.spi.OutingApplicationPort
import team.aliens.dms.domain.outing.spi.vo.OutingApplicationVO
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.persistence.outing.entity.QOutingApplicationJpaEntity.outingApplicationJpaEntity
import team.aliens.dms.persistence.outing.entity.QOutingCompanionJpaEntity.outingCompanionJpaEntity
import team.aliens.dms.persistence.outing.mapper.OutingApplicationMapper
import team.aliens.dms.persistence.outing.repository.OutingApplicationJpaRepository
import team.aliens.dms.persistence.outing.repository.vo.QQueryOutingApplicationVO
import team.aliens.dms.persistence.outing.repository.vo.QQueryOutingCompanionVO
import team.aliens.dms.persistence.outing.repository.vo.QueryOutingApplicationVO
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import java.time.LocalDate
import java.util.UUID

@Component
class OutingApplicationPersistenceAdapter(
    private val outingApplicationMapper: OutingApplicationMapper,
    private val outingApplicationRepository: OutingApplicationJpaRepository,
    private val queryFactory: JPAQueryFactory
) : OutingApplicationPort {

    override fun existOutingApplicationByOutAtAndStudentId(outAt: LocalDate, studentId: UUID) =
        outingApplicationRepository.existsByOutAtAndStudentId(outAt, studentId)

    override fun queryOutingApplicationById(outingApplicationId: UUID) =
        outingApplicationMapper.toDomain(
            outingApplicationRepository.findByIdOrNull(outingApplicationId)
        )

    override fun queryAllOutingApplicationVOsBetweenStartAndEnd(
        start: LocalDate,
        end: LocalDate,
    ): List<OutingApplicationVO> {
        val studentJpaEntity = QStudentJpaEntity("studentJpaEntity")
        val outingCompanionStudentJpaEntity = QStudentJpaEntity("outingCompanionStudentJpaEntity")

        return queryFactory
            .selectFrom(outingApplicationJpaEntity)
            .join(outingApplicationJpaEntity.student, studentJpaEntity)
            .leftJoin(outingCompanionJpaEntity)
            .on(outingApplicationJpaEntity.id.eq(outingCompanionJpaEntity.outingApplication.id))
            .leftJoin(outingCompanionJpaEntity.student, outingCompanionStudentJpaEntity)
            .where(outingApplicationJpaEntity.outAt.between(start, end))
            .orderBy(outingApplicationJpaEntity.outAt.asc())
            .transform(
                groupBy(outingApplicationJpaEntity.id)
                    .list(
                        QQueryOutingApplicationVO(
                            studentJpaEntity.name,
                            studentJpaEntity.grade,
                            studentJpaEntity.classRoom,
                            studentJpaEntity.number,
                            outingApplicationJpaEntity.outAt,
                            outingApplicationJpaEntity.outingTime,
                            outingApplicationJpaEntity.arrivalTime,
                            list(
                                QQueryOutingCompanionVO(
                                    outingCompanionStudentJpaEntity.name,
                                    outingCompanionStudentJpaEntity.grade,
                                    outingCompanionStudentJpaEntity.classRoom,
                                    outingCompanionStudentJpaEntity.number
                                )
                            )
                        )
                    )
            )
    }

        override fun queryCurrentOutingApplication(student: Student): OutingApplicationVO {
            return queryFactory
                .select(
                    QQueryOutingApplicationVO(
                        outingApplicationJpaEntity.outAt,
                        outingApplicationJpaEntity.outingType,
                        outingApplicationJpaEntity.status,
                        outingApplicationJpaEntity.outingTime,
                        outingApplicationJpaEntity.arrivalTime,
                        outingApplicationJpaEntity.destination,
                        outingApplicationJpaEntity.reason,
                        outingCompanionJpaEntity.student.name
                    )
                )
                .from(outingApplicationJpaEntity)
                .leftJoin(outingCompanionJpaEntity)
                .on(outingApplicationJpaEntity.id.eq(outingCompanionJpaEntity.id.outingApplicationId))
                .leftJoin(studentJpaEntity)
                .on(outingCompanionJpaEntity.id.studentId.eq(studentJpaEntity.id))
                .where(
                    outingApplicationJpaEntity.student.id.eq(student.id),
                    outingApplicationJpaEntity.status.ne(OutingStatus.DONE)
                )
                .fetchOne()
        }

    override fun saveOutingApplication(outingApplication: OutingApplication) =
        outingApplicationMapper.toDomain(
            outingApplicationRepository.save(
                outingApplicationMapper.toEntity(outingApplication)
            )
        )!!

    override fun deleteOutingApplication(outingApplication: OutingApplication) {
        outingApplicationRepository.delete(
            outingApplicationMapper.toEntity(outingApplication)
        )
    }
}
