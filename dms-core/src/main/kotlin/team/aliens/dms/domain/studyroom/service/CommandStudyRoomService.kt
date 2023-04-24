package team.aliens.dms.domain.studyroom.service

import java.util.UUID
import team.aliens.dms.domain.studyroom.model.AvailableTime
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.SeatType
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.StudyRoomTimeSlot
import team.aliens.dms.domain.studyroom.model.TimeSlot

interface CommandStudyRoomService {

    fun saveStudyRoom(studyRoom: StudyRoom): StudyRoom

    fun saveSeatType(seatType: SeatType): SeatType

    fun saveTimeSlot(timeSlot: TimeSlot): TimeSlot

    fun saveSeatApplication(seatApplication: SeatApplication): SeatApplication

    fun saveAllStudyRoomTimeSlots(studyRoomTimeSlots: List<StudyRoomTimeSlot>): List<StudyRoomTimeSlot>

    fun saveAllSeats(seats: List<Seat>): List<Seat>

    fun saveAvailableTime(availableTime: AvailableTime): AvailableTime

    fun deleteStudyRoom(studyRoomId: UUID)

    fun deleteTimeSlot(timeSlotId: UUID)

    fun deleteSeatApplication(studentId: UUID, seatId: UUID, timeSlotId: UUID)

    fun deleteSeatType(seatTypeId: UUID, schoolId: UUID)

    fun deleteSeatByStudyRoomId(studyRoomId: UUID)

    fun deleteAllSeatApplications()

    fun deleteSeatApplicationBySeatIdAndStudentIdAndTimeSlotId(seatId: UUID, id: UUID, timeSlotId: UUID)

    fun deleteStudyRoomTimeSlotByStudyRoomId(studyRoomId: UUID)

    fun updateTimeSlotsByStudyRoom(studyRoomId: UUID, studyRoomTimeSlots: List<StudyRoomTimeSlot>)

    fun updateSeatsByStudyRoom(studyRoomId: UUID, seats: List<Seat>)
}
