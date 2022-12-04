package team.aliens.dms.persistence.school.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.school.model.AvailableFeature
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import team.aliens.dms.persistence.school.entity.AvailableFeatureJpaEntity

@Component
class AvailableFeatureMapper(
    private val schoolJpaRepository: SchoolJpaRepository
) : GenericMapper<AvailableFeature, AvailableFeatureJpaEntity> {

    override fun toDomain(entity: AvailableFeatureJpaEntity?): AvailableFeature? {
        return entity?.let {
            AvailableFeature(
                schoolId = entity.schoolId,
                mealService = entity.mealService,
                noticeService = entity.noticeService,
                pointService = entity.pointService
            )
        }
    }

    override fun toEntity(domain: AvailableFeature): AvailableFeatureJpaEntity {
        val school = schoolJpaRepository.findByIdOrNull(domain.schoolId)

        return AvailableFeatureJpaEntity(
            schoolId = domain.schoolId,
            school = school,
            mealService = domain.mealService,
            noticeService = domain.noticeService,
            pointService = domain.pointService
        )
    }
}