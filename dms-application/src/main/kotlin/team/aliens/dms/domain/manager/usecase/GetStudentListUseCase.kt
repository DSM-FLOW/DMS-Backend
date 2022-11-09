package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.manager.dto.GetStudentListResponse
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException

@ReadOnlyUseCase
class GetStudentListUseCase(
    private val queryUserPort: ManagerQueryUserPort,
    private val queryStudentPort: ManagerQueryStudentPort
) {

    fun execute(name: String, sort: Sort): GetStudentListResponse {

        val user = queryUserPort.searchStudent(name, sort)
        val students = user.map {
            queryStudentPort.queryStudentById(it.id)

            val student = queryStudentPort.queryStudentById(it.id) ?: throw StudentNotFoundException
            GetStudentListResponse.StudentElement(
                id = it.id,
                name = it.name,
                gcn = StringUtil.gcnToString(
                    grade = student.grade,
                    classRoom = student.classRoom,
                    number = student.number
                ),
                roomNumber = student.roomNumber,
                profileImageUrl = it.profileImageUrl!!
            )
        }

        return GetStudentListResponse(students)
    }


}