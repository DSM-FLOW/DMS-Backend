package team.aliens.dms.domain.school.spi

import team.aliens.dms.domain.auth.spi.AuthQuerySchoolPort
import team.aliens.dms.domain.manager.spi.ManagerQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomAvailableTimeQuerySchoolPort

interface SchoolPort :
    QuerySchoolPort,
    CommandSchoolPort,
    ManagerQuerySchoolPort,
    StudentQuerySchoolPort,
    AuthQuerySchoolPort,
    StudyRoomAvailableTimeQuerySchoolPort {
}