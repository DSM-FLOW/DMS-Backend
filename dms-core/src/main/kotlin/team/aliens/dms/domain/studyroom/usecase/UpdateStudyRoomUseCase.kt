package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.dto.UpdateStudyRoomRequest
import team.aliens.dms.domain.studyroom.exception.StudyRoomAlreadyExistsException
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.model.StudyRoomTimeSlot
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.user.service.GetUserService
import team.aliens.dms.domain.user.spi.QueryUserPort

@UseCase
class UpdateStudyRoomUseCase(
    private val getUserService: GetUserService,
    private val queryStudyRoomPort: QueryStudyRoomPort,
    private val commandStudyRoomPort: CommandStudyRoomPort,
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort
) {

    fun execute(studyRoomId: UUID, request: UpdateStudyRoomRequest) {

        val user = getUserService.getCurrentUser()

        val studyRoom = queryStudyRoomPort.queryStudyRoomById(studyRoomId) ?: throw StudyRoomNotFoundException
        validateSameSchool(user.schoolId, studyRoom.schoolId)

        if (request.floor != studyRoom.floor || request.name != studyRoom.name) {
            val isAlreadyExists = queryStudyRoomPort.existsStudyRoomByFloorAndNameAndSchoolId(
                floor = request.floor,
                name = request.name,
                schoolId = user.schoolId
            )
            if (isAlreadyExists) {
                throw StudyRoomAlreadyExistsException
            }
        }

        val newStudyRoom = request.run {
            studyRoom.copy(
                name = name,
                floor = floor,
                widthSize = totalWidthSize,
                heightSize = totalHeightSize,
                availableHeadcount = seats.count {
                    SeatStatus.AVAILABLE == SeatStatus.valueOf(it.status)
                },
                availableSex = Sex.valueOf(request.availableSex),
                availableGrade = availableGrade,
                eastDescription = eastDescription,
                westDescription = westDescription,
                southDescription = southDescription,
                northDescription = northDescription
            )
        }
        val savedStudyRoom = commandStudyRoomPort.saveStudyRoom(newStudyRoom)

        commandStudyRoomPort.deleteStudyRoomTimeSlotByStudyRoomId(studyRoomId)
        val studyRoomTimeSlots = request.timeSlotIds.map {
            StudyRoomTimeSlot(
                studyRoomId = savedStudyRoom.id,
                timeSlotId = it
            )
        }
        commandStudyRoomPort.saveAllStudyRoomTimeSlots(studyRoomTimeSlots)

        commandStudyRoomPort.deleteSeatApplicationByStudyRoomId(studyRoomId)
        commandStudyRoomPort.deleteSeatByStudyRoomId(studyRoomId)
        val seats = request.seats.map {
            Seat(
                studyRoomId = studyRoom.id,
                typeId = it.typeId,
                widthLocation = it.widthLocation,
                heightLocation = it.heightLocation,
                number = it.number,
                status = SeatStatus.valueOf(it.status)
            )
        }
        commandStudyRoomPort.saveAllSeats(seats)
    }
}
