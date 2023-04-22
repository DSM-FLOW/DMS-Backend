package team.aliens.dms.domain.file.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.file.spi.ParseFilePort
import team.aliens.dms.domain.room.model.Room
import team.aliens.dms.domain.room.spi.FileCommandRoomPort
import team.aliens.dms.domain.room.spi.FileQueryRoomPort
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.student.exception.StudentAlreadyExistsException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.user.service.UserService
import java.io.File
import java.util.UUID

@UseCase
class ImportStudentUseCase(
    private val userService: UserService,
    private val parseFilePort: ParseFilePort,
    private val schoolService: SchoolService,
    private val fileCommandRoomPort: FileCommandRoomPort,
    private val fileQueryRoomPort: FileQueryRoomPort,
    private val commandStudentPort: CommandStudentPort,
    private val queryStudentPort: QueryStudentPort
) {

    fun execute(file: File) {
        val user = userService.getCurrentUser()
        val school = schoolService.getSchoolById(user.schoolId)

        val parsedStudentInfos = parseFilePort.getExcelStudentVO(file)

        val gcnList = parsedStudentInfos.map { Triple(it.grade, it.classRoom, it.number) }
        if (queryStudentPort.existsBySchoolIdAndGcnList(school.id, gcnList)) {
            throw StudentAlreadyExistsException
        }

        val roomMap = saveNotExistsRooms(
            roomNumbers = parsedStudentInfos.map { it.roomNumber }.distinctBy { it },
            schoolId = school.id
        )

        commandStudentPort.saveAllStudent(
            parsedStudentInfos.map {
                Student(
                    roomId = roomMap[it.roomNumber]!!.id,
                    roomNumber = it.roomNumber,
                    roomLocation = it.roomLocation,
                    schoolId = school.id,
                    grade = it.grade,
                    classRoom = it.classRoom,
                    number = it.number,
                    sex = it.sex,
                    name = it.name
                )
            }
        )
    }

    private fun saveNotExistsRooms(roomNumbers: List<String>, schoolId: UUID): MutableMap<String, Room> {

        val roomMap = fileQueryRoomPort.queryRoomsByRoomNumbersIn(roomNumbers, schoolId)
            .associateBy { it.number }
            .toMutableMap()

        val notExistsRooms = roomNumbers.mapNotNull { roomNumber ->
            if (!roomMap.containsKey(roomNumber)) {
                Room(
                    number = roomNumber,
                    schoolId = schoolId
                ).apply {
                    roomMap[roomNumber] = this
                }
            } else {
                null
            }
        }

        fileCommandRoomPort.saveRooms(notExistsRooms)
        return roomMap
    }
}
