package team.aliens.dms.persistence.student

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.Tuple
import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.core.types.Expression
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions.select
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.manager.dto.PointFilter
import team.aliens.dms.domain.manager.dto.PointFilterType
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.manager.spi.vo.StudentWithTag
import team.aliens.dms.domain.point.spi.vo.StudentWithPointVO
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.model.VerifiedStudent
import team.aliens.dms.domain.student.spi.StudentPort
import team.aliens.dms.persistence.point.entity.QPointHistoryJpaEntity.pointHistoryJpaEntity
import team.aliens.dms.persistence.room.entity.QRoomJpaEntity.roomJpaEntity
import team.aliens.dms.persistence.school.entity.QSchoolJpaEntity.schoolJpaEntity
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import team.aliens.dms.persistence.student.mapper.StudentMapper
import team.aliens.dms.persistence.student.mapper.VerifiedStudentMapper
import team.aliens.dms.persistence.student.repository.StudentJpaRepository
import team.aliens.dms.persistence.student.repository.VerifiedStudentJpaRepository
import team.aliens.dms.persistence.student.repository.vo.QQueryStudentWithPointVO
import team.aliens.dms.persistence.student.repository.vo.QQueryStudentsWithTagVO
import team.aliens.dms.persistence.tag.entity.QStudentTagJpaEntity.studentTagJpaEntity
import team.aliens.dms.persistence.tag.entity.QTagJpaEntity.tagJpaEntity
import team.aliens.dms.persistence.tag.mapper.TagMapper
import team.aliens.dms.persistence.user.entity.QUserJpaEntity.userJpaEntity
import java.util.UUID
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity
import team.aliens.dms.persistence.tag.entity.QStudentTagJpaEntity

@Component
class StudentPersistenceAdapter(
    private val studentMapper: StudentMapper,
    private val tagMapper: TagMapper,
    private val studentRepository: StudentJpaRepository,
    private val verifiedStudentRepository: VerifiedStudentJpaRepository,
    private val verifiedStudentMapper: VerifiedStudentMapper,
    private val queryFactory: JPAQueryFactory,
) : StudentPort {

    override fun existsStudentByGradeAndClassRoomAndNumber(
        grade: Int,
        classRoom: Int,
        number: Int
    ): Boolean = studentRepository.existsByGradeAndClassRoomAndNumber(grade, classRoom, number)

    override fun queryStudentBySchoolIdAndGcn(
        schoolId: UUID,
        grade: Int,
        classRoom: Int,
        number: Int,
    ) = studentMapper.toDomain(
        studentRepository.findByUserSchoolIdAndGradeAndClassRoomAndNumber(schoolId, grade, classRoom, number)
    )

    override fun queryStudentById(studentId: UUID) = studentMapper.toDomain(
        studentRepository.findByIdOrNull(studentId)
    )

    override fun existsBySchoolIdAndGcnList(schoolId: UUID, gcnList: List<Triple<Int, Int, Int>>): Boolean {
        return queryFactory
            .selectFrom(studentJpaEntity)
            .join(studentJpaEntity.user, userJpaEntity)
            .join(userJpaEntity.school, schoolJpaEntity)
            .where(
                schoolJpaEntity.id.eq(schoolId),
                Expressions.list(studentJpaEntity.grade, studentJpaEntity.classRoom, studentJpaEntity.number)
                    .`in`(*queryStudentGcnIn(gcnList))
            )
            .fetchFirst() != null
    }

    private fun queryStudentGcnIn(gcnList: List<Triple<Int, Int, Int>>): Array<Expression<Tuple>> {
        val tuple: MutableList<Expression<Tuple>> = ArrayList()
        for (gcn in gcnList) {
            tuple.add(
                Expressions.template(
                    Tuple::class.java,
                    "(({0}, {1}, {2}))",
                    gcn.first, gcn.second, gcn.third
                )
            )
        }

        return tuple.toTypedArray()
    }

    override fun saveStudent(student: Student) = studentMapper.toDomain(
        studentRepository.save(
            studentMapper.toEntity(student)
        )
    )!!

    override fun saveAllVerifiedStudent(verifiedStudents: List<VerifiedStudent>) {
        verifiedStudentRepository.saveAll(
            verifiedStudents.map {
                verifiedStudentMapper.toEntity(it)
            }
        )
    }

    override fun deleteVerifiedStudent(verifiedStudent: VerifiedStudent) {
        verifiedStudentRepository.delete(
            verifiedStudentMapper.toEntity(verifiedStudent)
        )
    }

    override fun queryStudentsByNameAndSortAndFilter(
        name: String?,
        sort: Sort,
        schoolId: UUID,
        pointFilter: PointFilter,
        tagIds: List<UUID>?
    ): List<StudentWithTag> {
        return queryFactory
            .selectFrom(studentJpaEntity)
            .join(studentJpaEntity.room, roomJpaEntity)
            .join(studentJpaEntity.user, userJpaEntity)
            .join(userJpaEntity.school, schoolJpaEntity)
            .leftJoin(studentTagJpaEntity)
            .on(studentTagJpaEntity.student.id.eq(studentJpaEntity.id))
            .leftJoin(tagJpaEntity).distinct()
            .on(eqTag())
            .leftJoin(pointHistoryJpaEntity)
            .on(eqStudentRecentPointHistory())
            .where(
                nameContains(name),
                pointTotalBetween(pointFilter),
                schoolEq(schoolId),
                tagIdsIn(tagIds)
            )
            .orderBy(
                sortFilter(sort),
                studentJpaEntity.grade.asc(),
                studentJpaEntity.classRoom.asc(),
                studentJpaEntity.number.asc()
            )
            .transform(
                groupBy(studentJpaEntity.id)
                    .list(
                        QQueryStudentsWithTagVO(
                            studentJpaEntity.id,
                            studentJpaEntity.name,
                            studentJpaEntity.grade,
                            studentJpaEntity.classRoom,
                            studentJpaEntity.number,
                            roomJpaEntity.number,
                            studentJpaEntity.profileImageUrl,
                            studentJpaEntity.sex,
                            list(tagJpaEntity)
                        )
                    )
            )
            .map {
                StudentWithTag(
                    id = it.id,
                    name = it.name,
                    grade = it.grade,
                    classRoom = it.classRoom,
                    number = it.number,
                    roomNumber = it.roomNumber,
                    profileImageUrl = it.profileImageUrl,
                    sex = it.sex,
                    tags = it.tags
                        .map {
                                tag ->
                            tagMapper.toDomain(tag)!!
                        }
                )
            }
    }

    private fun tagIdsIn(tagIds: List<UUID>?) =
        tagIds?.run { studentTagJpaEntity.tag.id.`in`(tagIds) }
        //if (tagIds?.isNotEmpty() == true) studentTagJpaEntity.tag.id.`in`(tagIds) else null

    private fun eqTag(): BooleanExpression? {
        return tagJpaEntity.id.`in`(
            select(studentTagJpaEntity.tag.id)
                .from(studentTagJpaEntity)
                .where(studentTagJpaEntity.student.id.eq(studentJpaEntity.id))
        )
    }

    private fun nameContains(name: String?) = name?.run { studentJpaEntity.name.contains(this) }

    private fun pointTotalBetween(pointFilter: PointFilter): BooleanExpression? {
        if (pointFilter.filterType == null) {
            return null
        }

        return when (pointFilter.filterType) {
            PointFilterType.BONUS -> {
                CaseBuilder()
                    .`when`(pointHistoryJpaEntity.isNotNull)
                    .then(pointHistoryJpaEntity.bonusTotal)
                    .otherwise(0).between(pointFilter.minPoint, pointFilter.maxPoint)
            }

            PointFilterType.MINUS -> {
                CaseBuilder()
                    .`when`(pointHistoryJpaEntity.isNotNull)
                    .then(pointHistoryJpaEntity.minusTotal)
                    .otherwise(0).between(pointFilter.minPoint, pointFilter.maxPoint)
            }

            else -> {
                CaseBuilder()
                    .`when`(pointHistoryJpaEntity.isNotNull)
                    .then(pointHistoryJpaEntity.bonusTotal.subtract(pointHistoryJpaEntity.minusTotal))
                    .otherwise(0).between(pointFilter.minPoint, pointFilter.maxPoint)
            }
        }
    }

    private fun schoolEq(schoolId: UUID) = userJpaEntity.school.id.eq(schoolId)

    private fun sortFilter(sort: Sort): OrderSpecifier<*>? {
        return when (sort) {
            Sort.NAME -> {
                studentJpaEntity.name.asc()
            }

            else -> {
                studentJpaEntity.grade.asc()
            }
        }
    }

    override fun queryUserByRoomNumberAndSchoolId(roomNumber: String, schoolId: UUID): List<Student> {
        return queryFactory
            .selectFrom(studentJpaEntity)
            .join(studentJpaEntity.room, roomJpaEntity)
            .join(studentJpaEntity.user, userJpaEntity)
            .where(
                roomJpaEntity.number.eq(roomNumber),
                userJpaEntity.school.id.eq(schoolId)
            ).fetch()
            .map {
                studentMapper.toDomain(it)!!
            }
    }

    override fun queryStudentsBySchoolId(schoolId: UUID): List<Student> {
        return queryFactory
            .selectFrom(studentJpaEntity)
            .join(studentJpaEntity.room, roomJpaEntity).fetchJoin()
            .join(studentJpaEntity.user, userJpaEntity).fetchJoin()
            .where(
                userJpaEntity.school.id.eq(schoolId)
            )
            .orderBy(roomJpaEntity.number.asc())
            .fetch()
            .map {
                studentMapper.toDomain(it)!!
            }
    }

    override fun queryStudentsWithPointHistory(studentIds: List<UUID>): List<StudentWithPointVO> {
        return queryFactory
            .select(
                QQueryStudentWithPointVO(
                    studentJpaEntity.name,
                    studentJpaEntity.grade,
                    studentJpaEntity.classRoom,
                    studentJpaEntity.number,
                    pointHistoryJpaEntity.bonusTotal,
                    pointHistoryJpaEntity.minusTotal
                )
            )
            .from(studentJpaEntity)
            .join(studentJpaEntity.user, userJpaEntity)
            .join(userJpaEntity.school, schoolJpaEntity)
            .leftJoin(pointHistoryJpaEntity)
            .on(eqStudentRecentPointHistory())
            .where(
                studentJpaEntity.id.`in`(studentIds)
            )
            .fetch()
            .map {
                StudentWithPointVO(
                    name = it.name,
                    grade = it.grade,
                    classRoom = it.classRoom,
                    number = it.number,
                    bonusTotal = it.bonusTotal ?: 0,
                    minusTotal = it.minusTotal ?: 0
                )
            }
    }

    private fun eqStudentRecentPointHistory(): BooleanExpression? {
        return pointHistoryJpaEntity.studentName.eq(studentJpaEntity.name)
            .and(eqGcn())
            .and(
                pointHistoryJpaEntity.createdAt.eq(
                    select(pointHistoryJpaEntity.createdAt.max())
                        .from(pointHistoryJpaEntity)
                        .where(
                            pointHistoryJpaEntity.school.id.eq(schoolJpaEntity.id),
                            pointHistoryJpaEntity.studentName.eq(studentJpaEntity.name),
                            eqGcn()
                        )
                )
            )
    }

    private fun eqGcn(): BooleanBuilder {
        val condition = BooleanBuilder()

        val gcn = pointHistoryJpaEntity.studentGcn
        condition.and(
            gcn.substring(0, 1).eq(studentJpaEntity.grade.stringValue())
        ).and(
            gcn.substring(1, 2).endsWith(studentJpaEntity.classRoom.stringValue())
        ).and(
            gcn.substring(2).endsWith(studentJpaEntity.number.stringValue())
        )

        return condition
    }

    override fun queryAllStudentsByIdsIn(studentIds: List<UUID>) =
        studentRepository.findAllByIdIn(studentIds)
            .map {
                studentMapper.toDomain(it)!!
            }
}
