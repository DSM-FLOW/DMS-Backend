package team.aliens.dms.global.filter

import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import team.aliens.dms.global.error.DmsException
import team.aliens.dms.global.error.ErrorProperty
import team.aliens.dms.global.error.ErrorResponse
import team.aliens.dms.global.exception.InternalServerErrorException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ExceptionFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            when(e) {
                is DmsException -> errorToJson(e.errorProperty, response)
                else -> {
                    errorToJson(InternalServerErrorException.EXCEPTION.errorProperty, response)
                    e.printStackTrace()
                }
            }
        }
    }

    private fun errorToJson(errorProperty: ErrorProperty, response: HttpServletResponse) {
        val errorResponse = ErrorResponse.of(errorProperty)

        response.status = errorResponse.status
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(ErrorResponse(errorProperty.status(), errorProperty.message()).toString())
    }
}