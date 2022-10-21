package team.aliens.dms.domain.manager.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.manager.dto.ResetManagerPasswordRequest
import team.aliens.dms.domain.auth.exception.AuthCodeNotMatchedException
import team.aliens.dms.domain.manager.exception.ManagerInfoNotMatchedException
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerCommandUserPort
import team.aliens.dms.domain.manager.spi.ManagerQueryAuthCodePort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.*

@ExtendWith(SpringExtension::class)
class ResetManagerPasswordUseCaseTest {

    @MockBean
    private lateinit var queryUserPort: ManagerQueryUserPort

    @MockBean
    private lateinit var queryAuthCodePort: ManagerQueryAuthCodePort

    @MockBean
    private lateinit var commandUserPort: ManagerCommandUserPort

    @MockBean
    private lateinit var securityPort: ManagerSecurityPort

    private lateinit var resetManagerPasswordUseCase: ResetManagerPasswordUseCase

    private val accountId = "dlwjddbs13"

    private val email = "dlwjddbs13@naver.com"

    private val code = "AUTH12"

    private val password = "이정윤비번"

    private val userId = UUID.randomUUID()

    private val user by lazy {
        User(
            id = userId,
            schoolId = UUID.randomUUID(),
            accountId = "111111",
            password = password,
            email = email,
            name = "이정윤",
            profileImageUrl = "http",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val authCode by lazy {
        AuthCode(
            code = code,
            userId = userId,
            type = EmailType.PASSWORD,
            expirationTime = 123
        )
    }

    private val request by lazy {
        ResetManagerPasswordRequest(
            accountId = accountId,
            email = email,
            authCode = code,
            newPassword = password
        )
    }

    private val accountIdUser by lazy {
        User(
            id = UUID.randomUUID(),
            schoolId = UUID.randomUUID(),
            accountId = "111111",
            password = password,
            email = email,
            name = "이정윤",
            profileImageUrl = "http",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val emailUser by lazy {
        User(
            id = UUID.randomUUID(),
            schoolId = UUID.randomUUID(),
            accountId = "111111",
            password = password,
            email = email,
            name = "이정윤",
            profileImageUrl = "http",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    @BeforeEach
    fun setUp() {
        resetManagerPasswordUseCase = ResetManagerPasswordUseCase(
            queryUserPort, queryAuthCodePort, commandUserPort, securityPort
        )
    }

    @Test
    fun `인증코드 일치`() {
        // given
        given(queryUserPort.queryByAccountId(request.accountId))
            .willReturn(user)

        given(queryUserPort.queryUserByEmail(request.email))
            .willReturn(user)

        given(queryAuthCodePort.queryAuthCodeByUserId(user.id))
            .willReturn(authCode)

        given(securityPort.encode(request.newPassword))
            .willReturn(password)

        // when & then
        assertDoesNotThrow {
            resetManagerPasswordUseCase.execute(request)
        }
    }

    @Test
    fun `아이디의 유저 존재하지 않음`() {
        // given
        given(queryUserPort.queryByAccountId(request.accountId))
            .willReturn(null)

        // when & then
        assertThrows<ManagerNotFoundException> {
            resetManagerPasswordUseCase.execute(request)
        }
    }

    @Test
    fun `이메일의 유저 존재하지 않음`() {
        // given
        given(queryUserPort.queryByAccountId(request.accountId))
            .willReturn(user)

        given(queryUserPort.queryUserByEmail(request.email))
            .willReturn(null)

        // when & then
        assertThrows<ManagerNotFoundException> {
            resetManagerPasswordUseCase.execute(request)
        }
    }

    @Test
    fun `관리자 정보 불일치`() {
        // given
        given(queryUserPort.queryByAccountId(request.accountId))
            .willReturn(accountIdUser)

        given(queryUserPort.queryUserByEmail(request.email))
            .willReturn(emailUser)

        // when & then
        assertThrows<ManagerInfoNotMatchedException> {
            resetManagerPasswordUseCase.execute(request)
        }
    }

    @Test
    fun `인증코드 존재하지 않음`() {
        // given
        given(queryUserPort.queryByAccountId(request.accountId))
            .willReturn(user)

        given(queryUserPort.queryUserByEmail(request.email))
            .willReturn(user)

        given(queryAuthCodePort.queryAuthCodeByUserId(user.id))
            .willReturn(null)

        // when & then
        assertThrows<AuthCodeNotFoundException> {
            resetManagerPasswordUseCase.execute(request)
        }
    }

    @Test
    fun `관리자 인증코드 불일치`() {
        // given
        given(queryUserPort.queryByAccountId(request.accountId))
            .willReturn(user)

        given(queryUserPort.queryUserByEmail(request.email))
            .willReturn(user)

        given(queryAuthCodePort.queryAuthCodeByUserId(user.id))
            .willReturn(authCode.copy(code = "이상한코드임"))

        // when & then
        assertThrows<AuthCodeNotMatchedException> {
            resetManagerPasswordUseCase.execute(request)
        }
    }
}