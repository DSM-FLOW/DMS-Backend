package team.aliens.dms.persistence.vote.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.manager.repository.ManagerJpaRepository
import team.aliens.dms.persistence.vote.entity.VotingTopicJpaEntity

@Component
class VotingTopicMapper(
    private val managerJpaRepository: ManagerJpaRepository
) : GenericMapper<VotingTopic, VotingTopicJpaEntity> {

    override fun toDomain(entity: VotingTopicJpaEntity?): VotingTopic? {
        return entity?.let {
            VotingTopic(
                id = it.id!!,
                topicName = it.topicName,
                startTime = it.startTime,
                endTime = it.endTime,
                voteType = it.voteType,
                managerId = it.manager!!.id!!
            )
        }
    }

    override fun toEntity(domain: VotingTopic): VotingTopicJpaEntity {
        val manager = managerJpaRepository.findByIdOrNull(domain.managerId)

        return VotingTopicJpaEntity(
            id = domain.id,
            topicName = domain.topicName,
            startTime = domain.startTime,
            endTime = domain.endTime,
            voteType = domain.voteType,
            manager = manager
        )
    }
}
