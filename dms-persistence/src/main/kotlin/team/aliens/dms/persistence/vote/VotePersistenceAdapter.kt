package team.aliens.dms.persistence.vote

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.VotePort
import team.aliens.dms.domain.vote.spi.vo.OptionVotingResultVO
import team.aliens.dms.domain.vote.spi.vo.StudentVotingResultVO
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import team.aliens.dms.persistence.vote.entity.QVoteJpaEntity.voteJpaEntity
import team.aliens.dms.persistence.vote.entity.QVotingOptionJpaEntity.votingOptionJpaEntity
import team.aliens.dms.persistence.vote.entity.QVotingTopicJpaEntity.votingTopicJpaEntity
import team.aliens.dms.persistence.vote.mapper.VoteMapper
import team.aliens.dms.persistence.vote.mapper.VotingOptionMapper
import team.aliens.dms.persistence.vote.mapper.VotingTopicMapper
import team.aliens.dms.persistence.vote.repository.VoteJpaRepository
import team.aliens.dms.persistence.vote.repository.VotingOptionJpaRepository
import team.aliens.dms.persistence.vote.repository.VotingTopicJpaRepository
import team.aliens.dms.persistence.vote.repository.vo.QQueryOptionVotingResult
import team.aliens.dms.persistence.vote.repository.vo.QQueryStudentVotingResultVO
import java.util.*

@Component
class VotePersistenceAdapter(
    private val voteMapper: VoteMapper,
    private val votingTopicMapper: VotingTopicMapper,
    private val votingOptionMapper: VotingOptionMapper,
    private val voteJpaRepository: VoteJpaRepository,
    private val votingTopicJpaRepository: VotingTopicJpaRepository,
    private val votingOptionJpaRepository: VotingOptionJpaRepository,
    private val queryFactory: JPAQueryFactory
) : VotePort {

    override fun saveVotingTopic(votingTopic: VotingTopic): VotingTopic = votingTopicMapper.toDomain(
        votingTopicJpaRepository.save(votingTopicMapper.toEntity(votingTopic))
    )!!

    override fun saveVote(vote: Vote): Vote = voteMapper.toDomain(
        voteJpaRepository.save(voteMapper.toEntity(vote))
    )!!

    override fun saveVotingOption(votingOption: VotingOption): VotingOption = votingOptionMapper.toDomain(
        votingOptionJpaRepository.save(votingOptionMapper.toEntity(votingOption))
    )!!
    override fun deleteVotingTopicByVotingTopicId(
        votingTopicId: UUID
    ) = votingTopicJpaRepository.deleteById(votingTopicId)

    override fun deleteVotingOptionByVotingOptionId(votingOptionId: UUID) = votingOptionJpaRepository.deleteById(votingOptionId)
    override fun deleteVoteById(voteId: UUID) = voteJpaRepository.deleteById(voteId)
    override fun queryVotingTopicById(votingTopicId: UUID): VotingTopic? = votingTopicMapper.toDomain(
        votingTopicJpaRepository.findByIdOrNull(votingTopicId)
    )

    override fun queryVotingOptionById(votingOptionId: UUID): VotingOption? = votingOptionMapper.toDomain(
        votingOptionJpaRepository.findByIdOrNull(votingOptionId)
    )

    override fun queryVoteById(voteId: UUID): Vote? = voteMapper.toDomain(
        voteJpaRepository.findByIdOrNull(voteId)
    )

    override fun queryAllVotingTopics(): List<VotingTopic>? {
        return queryFactory
            .selectFrom(votingTopicJpaEntity)
            .fetch()
            .map { entity ->
                VotingTopic(
                    id = entity.id!!,
                    topicName = entity.topicName,
                    startTime = entity.startTime,
                    endTime = entity.endTime,
                    voteType = entity.voteType,
                    managerId = entity.manager!!.id
                )
            }
    }

    override fun queryVotingOptionsByVotingTopicId(votingTopicId: UUID): List<VotingOption> {
        return queryFactory
            .selectFrom(votingOptionJpaEntity)
            .where(votingOptionJpaEntity.votingTopic.id.eq(votingTopicId))
            .fetch()
            .map { entity ->
                VotingOption(
                    id = entity.id!!,
                    votingTopicId = entity.votingTopic!!.id!!,
                    optionName = entity.optionName
                )
            }
    }

    override fun queryStudentVotingByVotingTopicIdAndGrade(votingTopicId: UUID, grade: Int): List<StudentVotingResultVO> {
        return queryFactory
            .select(
                QQueryStudentVotingResultVO(
                    studentJpaEntity.id,
                    studentJpaEntity.name,
                    voteJpaEntity.id.count().intValue()
                )
            )
            .from(voteJpaEntity)
            .join(voteJpaEntity.selectedStudent, studentJpaEntity)
            .join(voteJpaEntity.votingTopic, votingTopicJpaEntity)
            .where(
                votingTopicJpaEntity.id.eq(votingTopicId),
                studentJpaEntity.grade.eq(grade)
            )
            .groupBy(studentJpaEntity.id, studentJpaEntity.name)
            .orderBy(voteJpaEntity.id.count().intValue().desc())
            .fetch()
    }

    override fun queryOptionVotingByVotingTopicId(votingTopicId: UUID): List<OptionVotingResultVO> {
        return queryFactory
            .select(
                QQueryOptionVotingResult(
                    votingOptionJpaEntity.id,
                    votingOptionJpaEntity.optionName,
                    voteJpaEntity.id.count().intValue().coalesce(0) // Handle potential nulls
                )
            )
            .from(votingOptionJpaEntity)
            .leftJoin(voteJpaEntity).on(voteJpaEntity.selectedOption.eq(votingOptionJpaEntity))
            .where(votingOptionJpaEntity.votingTopic.id.eq(votingTopicId))
            .groupBy(votingOptionJpaEntity.id, votingOptionJpaEntity.optionName)
            .orderBy(voteJpaEntity.id.count().intValue().desc())
            .fetch()
    }

    override fun existVotingTopicByName(votingTopicName: String): Boolean =
        votingTopicJpaRepository.existsByTopicName(votingTopicName)

    override fun existVotingTopicById(votingTopicId: UUID): Boolean =
        votingTopicJpaRepository.existsById(votingTopicId)

    override fun existVotingOptionById(votingOptionId: UUID): Boolean =
        votingOptionJpaRepository.existsById(votingOptionId)

    override fun queryVoteByStudentId(studentId: UUID): List<Vote> =
        voteJpaRepository.findByStudentId(studentId).map {
                entity ->
            voteMapper.toDomain(entity)!!
        }
}
