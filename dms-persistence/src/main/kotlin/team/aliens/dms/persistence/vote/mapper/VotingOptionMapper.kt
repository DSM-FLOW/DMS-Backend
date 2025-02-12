package team.aliens.dms.persistence.vote.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.vote.entity.VotingOptionJpaEntity
import team.aliens.dms.persistence.vote.repository.VotingTopicJpaRepository

@Component
class VotingOptionMapper(
    private val votingTopicJpaRepository: VotingTopicJpaRepository
) : GenericMapper<VotingOption, VotingOptionJpaEntity> {

    override fun toDomain(entity: VotingOptionJpaEntity?): VotingOption? {
        return entity?.let {
            VotingOption(
                id = it.id!!,
                votingTopicId = it.votingTopic!!.id!!,
                optionName = it.optionName
            )
        }
    }

    override fun toEntity(domain: VotingOption): VotingOptionJpaEntity {
        val votingTopic = votingTopicJpaRepository.findByIdOrNull(domain.votingTopicId)

        return VotingOptionJpaEntity(
            id = domain.id,
            votingTopic = votingTopic,
            optionName = domain.optionName
        )
    }
}
