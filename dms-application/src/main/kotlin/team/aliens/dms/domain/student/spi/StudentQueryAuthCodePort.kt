package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.auth.model.AuthCode

interface StudentQueryAuthCodePort {
    fun queryAuthCodeByEmail(email: String): AuthCode?
}