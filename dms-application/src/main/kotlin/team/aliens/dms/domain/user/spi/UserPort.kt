package team.aliens.dms.domain.user.spi

import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort

interface UserPort : StudentQueryUserPort, ManagerQueryUserPort {
}