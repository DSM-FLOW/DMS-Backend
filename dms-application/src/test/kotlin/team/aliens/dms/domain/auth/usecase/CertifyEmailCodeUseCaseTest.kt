package team.aliens.dms.domain.auth.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.dto.CertifyEmailCodeRequest
import team.aliens.dms.domain.auth.exception.AuthCodeMismatchException
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.auth.spi.CommandAuthCodeLimitPort
import team.aliens.dms.domain.auth.spi.QueryAuthCodePort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.util.UUID

@ExtendWith(SpringExtension::class)
class CertifyEmailCodeUseCaseTest {

    @MockBean
    private lateinit var queryAuthCodePort: QueryAuthCodePort

    @MockBean
    private lateinit var queryUserPot: AuthQueryUserPort

    @MockBean
    private lateinit var commandAuthCodeLimitPort: CommandAuthCodeLimitPort

    private lateinit var certifyEmailCodeUseCase: CertifyEmailCodeUseCase

    @BeforeEach
    fun setUp() {
        certifyEmailCodeUseCase = CertifyEmailCodeUseCase(
            queryAuthCodePort,
            queryUserPot,
            commandAuthCodeLimitPort
        )
    }

    private val id = UUID.randomUUID()

    private val code = "123546"

    private val type = EmailType.PASSWORD

    private val email = "email@dsm.hs.kr"

    private val user by lazy {
        User(
            id = id,
            schoolId = id,
            accountId = "accountId",
            password = "password",
            email = email,
            name = "김범지인",
            profileImageUrl = "https://~~",
            createdAt = null,
            deletedAt = null
        )
    }

    private val authCode by lazy {
        AuthCode(
            code = code,
            userId = id,
            type = type,
            expirationTime = 0
        )
    }

    @Test
    fun `이메일코드 확인 성공`() {
        val request = CertifyEmailCodeRequest(
            email, code, type
        )

        // given
        given(queryUserPot.queryUserByEmail(email))
            .willReturn(user)

        given(
            queryAuthCodePort.queryAuthCodeByUserIdAndEmailType(
                id,
                type
            )
        ).willReturn(authCode)

        // when & then
        assertDoesNotThrow {
            certifyEmailCodeUseCase.execute(request)
        }
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        val request = CertifyEmailCodeRequest(
            email, code, type
        )

        // given
        given(queryUserPot.queryUserByEmail(email))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            certifyEmailCodeUseCase.execute(request)
        }
    }

    @Test
    fun `인증코드를 찾을 수 없음`() {
        val request = CertifyEmailCodeRequest(
            email, code, type
        )

        // given
        given(queryUserPot.queryUserByEmail(email))
            .willReturn(user)

        given(
            queryAuthCodePort.queryAuthCodeByUserIdAndEmailType(
                id,
                type
            )
        ).willReturn(null)

        // when & then
        assertThrows<AuthCodeNotFoundException> {
            certifyEmailCodeUseCase.execute(request)
        }
    }

    @Test
    fun `인증코드 일치하지 않음`() {
        val notMatchedCode = "!@QWER"
        val request = CertifyEmailCodeRequest(
            email, notMatchedCode, type
        )

        // given
        given(queryUserPot.queryUserByEmail(email))
            .willReturn(user)

        given(
            queryAuthCodePort.queryAuthCodeByUserIdAndEmailType(
                id,
                type
            )
        ).willReturn(authCode)

        //when & then
        assertThrows<AuthCodeMismatchException> {
            certifyEmailCodeUseCase.execute(request)
        }
    }
}