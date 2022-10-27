package team.aliens.dms.global.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import team.aliens.dms.domain.auth.model.Authority.MANAGER
import team.aliens.dms.domain.auth.model.Authority.STUDENT
import team.aliens.dms.global.filter.FilterConfig
import team.aliens.dms.global.security.token.JwtParser

@Configuration
class SecurityConfiguration(
    private val jwtParser: JwtParser,
    private val objectMapper: ObjectMapper
) {

    @Bean
    protected fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .cors().and()

        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http
            .authorizeRequests()

            // /auth
            .antMatchers(HttpMethod.GET, "/auth/account-id").permitAll()
            .antMatchers(HttpMethod.GET, "/auth/email").permitAll()
            .antMatchers(HttpMethod.GET, "/auth/code").permitAll()
            .antMatchers(HttpMethod.POST, "/auth/code").permitAll()
            .antMatchers(HttpMethod.POST, "/auth/tokens").permitAll()
            .antMatchers(HttpMethod.PUT, "/auth/reissue").permitAll()

            // /students
            .antMatchers(HttpMethod.GET, "/students/email/duplication").permitAll()
            .antMatchers(HttpMethod.GET, "/students/account-id/duplication").permitAll()
            .antMatchers(HttpMethod.GET, "/students/account-id/{school-id}").permitAll()
            .antMatchers(HttpMethod.POST, "/students/signup").permitAll()
            .antMatchers(HttpMethod.PATCH, "/students/password/initialization").permitAll()

            // /managers
            .antMatchers(HttpMethod.GET, "/managers/account-id/{school-id}").permitAll()
            .antMatchers(HttpMethod.PATCH, "/managers/password/initialization").permitAll()
            .antMatchers(HttpMethod.GET, "/managers/students/{student-id}").hasAuthority(MANAGER.name)

            // /schools
            .antMatchers(HttpMethod.GET, "/schools").permitAll()
            .antMatchers(HttpMethod.GET, "/schools/question/{school-id}").permitAll()
            .antMatchers(HttpMethod.GET, "/schools/answer/{school-id}").permitAll()
            .antMatchers(HttpMethod.GET, "/schools/code").permitAll()

            // /notices
            .antMatchers(HttpMethod.GET, "/notices/status").hasAuthority(STUDENT.name)

            // /files
            .antMatchers(HttpMethod.POST, "/files").permitAll()

            // /meals
            .antMatchers(HttpMethod.GET, "/meals/{date}").hasAuthority(STUDENT.name)

        http
            .apply(FilterConfig(jwtParser, objectMapper))

        return http.build()
    }

    @Bean
    protected fun passwordEncoder() = BCryptPasswordEncoder()
}