package team.aliens.dms.persistence.vote

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.vote.model.ModelStudent
import team.aliens.dms.domain.vote.spi.ModelStudentListPort
import team.aliens.dms.persistence.point.entity.QPointHistoryJpaEntity
import team.aliens.dms.persistence.student.repository.StudentJpaRepository
import java.time.LocalDateTime

@Component
class ModelStudentListAdapter(
    private val queryFactory: JPAQueryFactory,
    private val studentJpaRepository: StudentJpaRepository
) : ModelStudentListPort {

    override fun findModelStudents(startOfDay: LocalDateTime, endOfDay: LocalDateTime): List<ModelStudent> {

        val penalizedStudentGcn = findPenalizedStudentGcn(startOfDay, endOfDay)

        val students = studentJpaRepository.findAll()

        return students
            .map { student ->
                val gcn = createGcn(student.grade, student.classRoom, student.number)
                ModelStudent(
                    id = student.id ?: throw IllegalStateException("학생 ID가 존재하지 않습니다."),
                    studentGcn = gcn,
                    studentName = student.name,
                    studentProfile = student.profileImageUrl
                )
            }
            .filter { it.studentGcn !in penalizedStudentGcn }
    }

    private fun findPenalizedStudentGcn(startOfDay: LocalDateTime, endOfDay: LocalDateTime): List<String> {
        return queryFactory
            .select(
                QPointHistoryJpaEntity.pointHistoryJpaEntity.studentGcn
            )
            .from(QPointHistoryJpaEntity.pointHistoryJpaEntity)
            .where(
                QPointHistoryJpaEntity.pointHistoryJpaEntity.isCancel.isFalse,
                QPointHistoryJpaEntity.pointHistoryJpaEntity.pointType.eq(PointType.MINUS),
                QPointHistoryJpaEntity.pointHistoryJpaEntity.createdAt.between(startOfDay, endOfDay)
            )
            .fetch()
    }

    private fun createGcn(grade: Int, classRoom: Int, number: Int): String {
        val formattedNumber = String.format("%02d", number)
        return "$grade$classRoom$formattedNumber"
    }
}
