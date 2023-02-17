package team.aliens.dms.persistence.point

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PointPort
import team.aliens.dms.persistence.point.entity.QPointHistoryJpaEntity.pointHistoryJpaEntity
import team.aliens.dms.persistence.point.mapper.PointHistoryMapper
import team.aliens.dms.persistence.point.repository.PointHistoryJpaRepository
import team.aliens.dms.persistence.point.repository.vo.QQueryPointHistoryVO
import team.aliens.dms.persistence.point.repository.vo.QueryAllPointHistoryVO

@Component
class PointPersistenceAdapter(
    private val pointHistoryMapper: PointHistoryMapper,
    private val pointHistoryRepository: PointHistoryJpaRepository,
    private val queryFactory: JPAQueryFactory
) : PointPort {

    override fun queryPointHistoryById(pointHistoryId: UUID) = pointHistoryMapper.toDomain(
        pointHistoryRepository.findByIdOrNull(pointHistoryId)
    )

    override fun queryBonusAndMinusTotalPointByGcnAndStudentName(
        gcn: String,
        studentName: String,
    ): Pair<Int, Int> {
        val lastHistory = queryFactory
            .selectFrom(pointHistoryJpaEntity)
            .orderBy(pointHistoryJpaEntity.createdAt.desc())
            .where(
                pointHistoryJpaEntity.gcn.eq(gcn),
                pointHistoryJpaEntity.name.eq(studentName)
            )
            .limit(1)
            .fetchOne()

        val bonusTotal = lastHistory?.bonusTotal ?: 0
        val minusTotal = lastHistory?.bonusTotal ?: 0

        return Pair(bonusTotal, minusTotal)
    }

    override fun queryPointHistoryByGcnAndStudentNameAndType(
        gcn: String,
        studentName: String,
        type: PointType,
        isCancel: Boolean?
    ): List<QueryPointHistoryResponse.Point> {
        return queryFactory
            .select(
                QQueryPointHistoryVO(
                    pointHistoryJpaEntity.id,
                    pointHistoryJpaEntity.createdAt!!,
                    pointHistoryJpaEntity.type,
                    pointHistoryJpaEntity.name,
                    pointHistoryJpaEntity.score
                )
            )
            .from(pointHistoryJpaEntity)
            .where(
                pointHistoryJpaEntity.isCancel.eq(false),
                pointHistoryJpaEntity.gcn.eq(gcn),
                pointHistoryJpaEntity.name.eq(studentName),
                pointHistoryJpaEntity.type.eq(type),
                isCancel?.let { pointHistoryJpaEntity.isCancel.eq(it) }
            )
            .orderBy(pointHistoryJpaEntity.createdAt.desc())
            .fetch()
            .map {
                QueryPointHistoryResponse.Point(
                    pointHistoryId = it.pointId,
                    date = it.date.toLocalDate(),
                    type = it.type,
                    name = it.name,
                    score = it.score
                )
            }
    }

    override fun queryPointHistoryByGcnAndStudentName(
        gcn: String,
        studentName: String,
        isCancel: Boolean?
    ): List<QueryPointHistoryResponse.Point> {
        return queryFactory
            .select(
                QQueryPointHistoryVO(
                    pointHistoryJpaEntity.id,
                    pointHistoryJpaEntity.createdAt!!,
                    pointHistoryJpaEntity.type,
                    pointHistoryJpaEntity.name,
                    pointHistoryJpaEntity.score
                )
            )
            .from(pointHistoryJpaEntity)
            .where(
                pointHistoryJpaEntity.isCancel.eq(false),
                pointHistoryJpaEntity.gcn.eq(gcn),
                pointHistoryJpaEntity.name.eq(studentName),
                isCancel?.let { pointHistoryJpaEntity.isCancel.eq(it) }
            )
            .orderBy(pointHistoryJpaEntity.createdAt.desc())
            .fetch()
            .map {
                QueryPointHistoryResponse.Point(
                    pointHistoryId = it.pointId,
                    date = it.date.toLocalDate(),
                    type = it.type,
                    name = it.name,
                    score = it.score
                )
            }
    }
}