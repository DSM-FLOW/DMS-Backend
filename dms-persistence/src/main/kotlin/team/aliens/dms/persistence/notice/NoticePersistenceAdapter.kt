package team.aliens.dms.persistence.notice

import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.model.OrderType
import team.aliens.dms.domain.notice.spi.NoticePort
import team.aliens.dms.persistence.manager.repository.ManagerJpaRepository
import team.aliens.dms.persistence.notice.entity.NoticeJpaEntity
import team.aliens.dms.persistence.notice.mapper.NoticeMapper
import team.aliens.dms.persistence.notice.repository.NoticeJpaRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.UUID

@Component
class NoticePersistenceAdapter(
    private val noticeRepository: NoticeJpaRepository,
    private val noticeMapper: NoticeMapper,
    private val taskSchduler: ThreadPoolTaskScheduler,
    private val managerJpaRepository: ManagerJpaRepository
) : NoticePort {

    override fun existsNoticeByDateBetween(from: LocalDate, to: LocalDate): Boolean {
        val fromLocalDateTime = from.atTime(LocalTime.now())
        val toLocalDateTime = to.atTime(LocalTime.now())

        return noticeRepository.existsByCreatedAtBetween(fromLocalDateTime, toLocalDateTime)
    }

    override fun queryNoticeById(noticeId: UUID) = noticeMapper.toDomain(
        noticeRepository.findByIdOrNull(noticeId)
    )

    override fun queryAllNoticesBySchoolIdAndOrder(schoolId: UUID, orderType: OrderType): List<Notice> {
        return when (orderType) {
            OrderType.NEW -> noticeRepository.findAllByManagerUserSchoolIdOrderByCreatedAtDesc(schoolId)
            OrderType.OLD -> noticeRepository.findAllByManagerUserSchoolIdOrderByCreatedAtAsc(schoolId)
        }.map {
            noticeMapper.toDomain(it)!!
        }
    }

    override fun queryNoticeByIdAndManagerId(noticeId: UUID, managerId: UUID) = noticeMapper.toDomain(
        noticeRepository.findByIdAndManagerUserId(noticeId, managerId)
    )

    override fun deleteNotice(notice: Notice) {
        noticeRepository.delete(
            noticeMapper.toEntity(notice)
        )
    }

    override fun saveNotice(notice: Notice) = noticeMapper.toDomain(
        noticeRepository.save(
            noticeMapper.toEntity(notice)
        )
    )!!

    override fun scheduleVoteResultNoticeDelivery(endTime: LocalDateTime, managerId: UUID, title: String, content: String) {

        val manager = managerJpaRepository.findByIdOrNull(managerId)
        val endTimeInstant = endTime.atZone(ZoneId.systemDefault()).toInstant()

        taskSchduler.schedule(
            {
                noticeRepository.save(
                    NoticeJpaEntity(
                        id = null,
                        manager = manager,
                        title = title,
                        content = content,
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    )
                )
            }, endTimeInstant
        )
    }
}
