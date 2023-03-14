package team.aliens.dms.persistence.studyroom

import com.querydsl.core.types.Expression
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.StudyRoomTimeSlot
import team.aliens.dms.domain.studyroom.spi.StudyRoomPort
import team.aliens.dms.domain.studyroom.spi.vo.SeatApplicationVO
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QSeatApplicationJpaEntity.seatApplicationJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QSeatJpaEntity.seatJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QSeatTypeJpaEntity.seatTypeJpaEntity
import team.aliens.dms.persistence.studyroom.entity.QStudyRoomJpaEntity.studyRoomJpaEntity
import team.aliens.dms.persistence.studyroom.mapper.SeatApplicationMapper
import team.aliens.dms.persistence.studyroom.mapper.SeatMapper
import team.aliens.dms.persistence.studyroom.mapper.StudyRoomMapper
import team.aliens.dms.persistence.studyroom.mapper.StudyRoomTimeSlotMapper
import team.aliens.dms.persistence.studyroom.repository.SeatApplicationJpaRepository
import team.aliens.dms.persistence.studyroom.repository.SeatJpaRepository
import team.aliens.dms.persistence.studyroom.repository.StudyRoomJpaRepository
import team.aliens.dms.persistence.studyroom.repository.StudyRoomTimeSlotJpaRepository
import team.aliens.dms.persistence.studyroom.repository.vo.QQuerySeatApplicationVO
import team.aliens.dms.persistence.studyroom.repository.vo.QQueryStudyRoomVO
import java.util.UUID

@Component
class StudyRoomPersistenceAdapter(
    private val studyRoomMapper: StudyRoomMapper,
    private val seatMapper: SeatMapper,
    private val studyRoomTimeSlotMapper: StudyRoomTimeSlotMapper,
    private val seatApplicationMapper: SeatApplicationMapper,
    private val studyRoomRepository: StudyRoomJpaRepository,
    private val seatRepository: SeatJpaRepository,
    private val studyRoomTimeSlotRepository: StudyRoomTimeSlotJpaRepository,
    private val seatApplicationRepository: SeatApplicationJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : StudyRoomPort {

    override fun queryStudyRoomById(studyRoomId: UUID) = studyRoomMapper.toDomain(
        studyRoomRepository.findByIdOrNull(studyRoomId)
    )

    override fun querySeatById(seatId: UUID) = seatMapper.toDomain(
        seatRepository.findByIdOrNull(seatId)
    )

    override fun querySeatApplicationByStudentId(studentId: UUID) = seatApplicationMapper.toDomain(
        seatApplicationRepository.queryByStudentId(studentId)
    )

    override fun queryStudyRoomsBySchoolId(schoolId: UUID) =
        studyRoomRepository.findBySchoolId(schoolId)
            .map { studyRoomMapper.toDomain(it)!! }

    override fun existsStudyRoomByFloorAndNameAndSchoolId(floor: Int, name: String, schoolId: UUID) =
        studyRoomRepository.existsByNameAndFloorAndSchoolId(name, floor, schoolId)

    override fun queryAllSeatApplicationVOsByStudyRoomIdAndTimeSlotId(
        studyRoomId: UUID,
        timeSlotId: UUID?,
    ): List<SeatApplicationVO> {
        return jpaQueryFactory
            .select(
                QQuerySeatApplicationVO(
                    seatJpaEntity.id,
                    seatJpaEntity.widthLocation,
                    seatJpaEntity.heightLocation,
                    seatJpaEntity.number,
                    seatJpaEntity.status,
                    seatTypeJpaEntity.id,
                    seatTypeJpaEntity.name,
                    seatTypeJpaEntity.color,
                    studentJpaEntity.id,
                    studentJpaEntity.name,
                    studentJpaEntity.grade,
                    studentJpaEntity.classRoom,
                    studentJpaEntity.number,
                    studentJpaEntity.profileImageUrl
                )
            )
            .from(seatJpaEntity)
            .join(seatJpaEntity.studyRoom, studyRoomJpaEntity)
            .leftJoin(seatJpaEntity.type, seatTypeJpaEntity)
            .leftJoin(seatApplicationJpaEntity)
            .on(
                seatJpaEntity.id.eq(seatApplicationJpaEntity.seat.id),
                seatApplicationJpaEntity.timeSlot.id.eq(timeSlotId)
            )
            .leftJoin(seatApplicationJpaEntity.student, studentJpaEntity)
            .where(
                seatJpaEntity.studyRoom.id.eq(studyRoomId)
            )
            .fetch()
    }

    override fun queryAllStudyRoomsByTimeSlotId(timeSlotId: UUID?): List<StudyRoomVO> {
        return jpaQueryFactory
            .select(
                QQueryStudyRoomVO(
                    studyRoomJpaEntity.id,
                    studyRoomJpaEntity.floor,
                    studyRoomJpaEntity.name,
                    studyRoomJpaEntity.availableGrade,
                    studyRoomJpaEntity.availableSex,
                    seatApplicationJpaEntity.count().intValue() as Expression<Int>,
                    studyRoomJpaEntity.availableHeadcount
                )
            )
            .from(studyRoomJpaEntity)
            .leftJoin(seatJpaEntity).on(studyRoomJpaEntity.id.eq(seatJpaEntity.studyRoom.id))
            .leftJoin(seatApplicationJpaEntity).on(
                seatJpaEntity.id.eq(seatApplicationJpaEntity.seat.id),
                seatApplicationJpaEntity.timeSlot.id.eq(timeSlotId)
            )
            .groupBy(studyRoomJpaEntity.id)
            .orderBy(
                studyRoomJpaEntity.floor.asc(),
                studyRoomJpaEntity.name.asc()
            )
            .fetch()
    }

    override fun queryTimeSlotsBySchoolId(schoolId: UUID) =
        studyRoomTimeSlotRepository.findBySchoolId(schoolId)
            .map { studyRoomTimeSlotMapper.toDomain(it)!! }

    override fun queryTimeSlotById(timeSlotId: UUID) = studyRoomTimeSlotMapper.toDomain(
        studyRoomTimeSlotRepository.findByIdOrNull(timeSlotId)
    )

    override fun existsTimeSlotById(timeSlotId: UUID) =
        studyRoomTimeSlotRepository.existsById(timeSlotId)

    override fun existsTimeSlotsBySchoolId(schoolId: UUID) =
        studyRoomTimeSlotRepository.existsBySchoolId(schoolId)

    override fun queryAllSeatApplicationByTimeSlotId(timeSlotId: UUID?) =
        jpaQueryFactory.selectFrom(seatApplicationJpaEntity)
            .where(
                seatApplicationTimeSlotIdEqOrIsNull(timeSlotId)
            )
            .fetch()
            .map {
                seatApplicationMapper.toDomain(it)!!
            }

    private fun seatApplicationTimeSlotIdEqOrIsNull(timeSlotId: UUID?): BooleanExpression? =
        if (timeSlotId != null) seatApplicationJpaEntity.timeSlot.id.eq(timeSlotId) else seatApplicationJpaEntity.isNull

    override fun existsSeatApplicationBySeatIdAndTimeSlotId(seatId: UUID, timeSlotId: UUID?) =
        jpaQueryFactory.selectFrom(seatApplicationJpaEntity)
            .where(
                seatApplicationJpaEntity.seat.id.eq(seatId),
                seatApplicationTimeSlotIdEqOrIsNull(timeSlotId)
            )
            .fetchFirst() != null

    override fun saveSeat(seat: Seat) = seatMapper.toDomain(
        seatRepository.save(
            seatMapper.toEntity(seat)
        )
    )!!

    override fun saveAllSeats(seats: List<Seat>) =
        seatRepository.saveAll(
            seats.map { seatMapper.toEntity(it) }
        ).map { seatMapper.toDomain(it)!! }

    override fun saveTimeSlot(timeSlot: StudyRoomTimeSlot) = studyRoomTimeSlotMapper.toDomain(
        studyRoomTimeSlotRepository.save(
            studyRoomTimeSlotMapper.toEntity(timeSlot)
        )
    )!!

    override fun saveSeatApplication(seatApplication: SeatApplication) = seatApplicationMapper.toDomain(
        seatApplicationRepository.save(
            seatApplicationMapper.toEntity(seatApplication)
        )
    )!!

    override fun saveStudyRoom(studyRoom: StudyRoom) = studyRoomMapper.toDomain(
        studyRoomRepository.save(
            studyRoomMapper.toEntity(studyRoom)
        )
    )!!

    override fun deleteStudyRoomById(studyRoomId: UUID) {
        studyRoomRepository.deleteById(studyRoomId)
    }

    override fun deleteTimeSlotById(studyRoomTimeSlotId: UUID) {
        studyRoomTimeSlotRepository.deleteById(studyRoomTimeSlotId)
    }

    override fun deleteSeatApplications(seatApplicationIds: List<UUID>) {
        seatApplicationRepository.deleteAllById(seatApplicationIds)
    }

    override fun deleteSeatApplicationByStudentId(studentId: UUID) {
        seatApplicationRepository.deleteByStudentId(studentId)
    }

    override fun deleteSeatApplicationByTimeSlotId(timeSlotId: UUID) {
        seatApplicationRepository.deleteByTimeSlotId(timeSlotId)
    }

    override fun deleteSeatApplicationByStudyRoomId(studyRoomId: UUID) {
        jpaQueryFactory
            .delete(seatApplicationJpaEntity)
            .where(
                seatApplicationJpaEntity.seat.studyRoom.id.eq(studyRoomId)
            )
    }

    override fun deleteSeatByStudyRoomId(studyRoomId: UUID) {
        seatRepository.deleteByStudyRoomId(studyRoomId)
    }

    override fun deleteAllSeatApplications() {
        seatApplicationRepository.deleteAll()
    }

    override fun existsSeatBySeatTypeId(seatTypeId: UUID) = seatRepository.existsByTypeId(seatTypeId)
}
