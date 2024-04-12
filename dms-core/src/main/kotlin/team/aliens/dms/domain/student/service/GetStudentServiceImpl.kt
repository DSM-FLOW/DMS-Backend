package team.aliens.dms.domain.student.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.file.spi.vo.ExcelStudentVO
import team.aliens.dms.domain.manager.dto.PointFilter
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.room.exception.RoomNotFoundException
import team.aliens.dms.domain.room.model.Room
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.exception.StudentUpdateInfoNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.QueryStudentPort
import java.util.UUID
import java.util.function.Function

@Service
class GetStudentServiceImpl(
    private val securityPort: SecurityPort,
    private val queryStudentPort: QueryStudentPort,
    private val queryPointHistoryPort: QueryPointHistoryPort
) : GetStudentService {

    override fun getCurrentStudent(): Student {
        val currentUserId = securityPort.getCurrentUserId()
        return queryStudentPort.queryStudentByUserId(currentUserId) ?: throw StudentNotFoundException
    }

    override fun getStudentBySchoolIdAndGcn(schoolId: UUID, grade: Int, classRoom: Int, number: Int) =
        queryStudentPort.queryStudentBySchoolIdAndGcn(schoolId, grade, classRoom, number)
            ?: throw StudentNotFoundException

    override fun getStudentsBySchoolIdAndGcnIn(schoolId: UUID, gcnList: List<Triple<Int, Int, Int>>) =
        queryStudentPort.queryBySchoolIdAndGcnIn(schoolId, gcnList)

    override fun getStudentsByRoomNumberAndRoomLocationIn(
        schoolId: UUID,
        roomNumberLocations: List<Pair<String, String>>,
    ) = queryStudentPort.queryBySchoolIdAndRoomNumberAndRoomLocationIn(schoolId, roomNumberLocations)

    override fun getStudentById(studentId: UUID) =
        queryStudentPort.queryStudentById(studentId) ?: throw StudentNotFoundException

    override fun getStudentByUserId(userId: UUID) =
        queryStudentPort.queryStudentByUserId(userId) ?: throw StudentNotFoundException

    override fun getStudentsByNameAndSortAndFilter(
        name: String?,
        sort: Sort,
        schoolId: UUID,
        pointFilter: PointFilter,
        tagIds: List<UUID>?,
    ) = queryStudentPort.queryStudentsByNameAndSortAndFilter(name, sort, schoolId, pointFilter, tagIds)

    override fun getRoommates(studentId: UUID, roomNumber: String, schoolId: UUID): List<Student> {
        return queryStudentPort.queryStudentsByRoomNumberAndSchoolId(roomNumber, schoolId)
            .filter {
                it.id != studentId
            }
    }

    override fun getStudentsWithPointHistory(studentIds: List<UUID>) =
        queryStudentPort.queryStudentsWithPointHistory(studentIds)
            .apply { if (size != studentIds.size) throw StudentNotFoundException }

    override fun getStudentsBySchoolId(schoolId: UUID) =
        queryStudentPort.queryStudentsBySchoolId(schoolId)

    override fun getAllStudentWithMinusPoint(): List<Pair<UUID, Int>> =
        queryStudentPort.queryAllStudentsByName("").map { student ->
            val minusTotalPoint = queryPointHistoryPort.queryBonusAndMinusTotalPointByStudentGcnAndName(
                gcn = student.gcn,
                studentName = student.name
            ).second

            Pair(student.id, minusTotalPoint)
        }

    override fun getAllStudentsByIdsIn(studentIds: List<UUID>) =
        queryStudentPort.queryAllStudentsByIdsIn(studentIds)
            .also { students ->
                if (!students.map { it.id }.containsAll(studentIds)) {
                    throw StudentNotFoundException
                }
            }

    override fun getRoomUpdatedStudent(
        roomMap: Map<String, Room>,
        studentMap: Map<Triple<Int, Int, Int>, Student>,
        studentVOs: List<ExcelStudentVO>,
    ): List<Student> =
        getUpdatedStudent(
            studentVOs = studentVOs,
        ) { studentVO ->
            val student = studentMap[studentVO.tripleGcn].also {
                if (it == null || it.name != studentVO.name) throw StudentNotFoundException
            }!!
            val room = roomMap[studentVO.roomNumber] ?: throw RoomNotFoundException
            student.copy(
                roomId = room.id,
                roomNumber = room.number,
                roomLocation = studentVO.roomLocation,
            )
        }

    override fun getAllStudentsByName(name: String?) =
        queryStudentPort.queryAllStudentsByName(name)

    override fun getGcnUpdatedStudent(
        studentMap: Map<Pair<String, String>, Student>,
        studentVOs: List<ExcelStudentVO>,
    ): List<Student> =
        getUpdatedStudent(
            studentVOs = studentVOs
        ) { studentVO ->
            val student = studentMap[studentVO.pairRoomNumberAndLocation].also {
                if (it == null || it.name != studentVO.name) throw StudentNotFoundException
            }!!
            student.copy(
                grade = studentVO.grade,
                classRoom = studentVO.classRoom,
                number = studentVO.number
            )
        }

    private fun getUpdatedStudent(
        studentVOs: List<ExcelStudentVO>,
        updateStudent: Function<ExcelStudentVO, Student>,
    ): List<Student> {
        val invalidStudentNames = mutableListOf<String>()

        return studentVOs.mapNotNull { studentVO ->
            try {
                updateStudent.apply(studentVO)
            } catch (e: Exception) {
                invalidStudentNames.add(studentVO.name)
                null
            }
        }.apply {
            if (invalidStudentNames.isNotEmpty()) {
                throw StudentUpdateInfoNotFoundException(invalidStudentNames)
            }
        }
    }
}
