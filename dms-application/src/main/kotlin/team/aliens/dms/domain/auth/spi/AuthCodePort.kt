package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.student.spi.StudentQueryAuthPort

interface AuthCodePort : QueryAuthPort, StudentQueryAuthPort {
}