package site.jongin.springjwtstudykotlin.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpMethod
import org.springframework.security.core.Authentication
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import site.jongin.springjwtstudykotlin.dto.request.LoginRequest
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginAuthenticationProcessingFilter (
    loginPath: String,
    private val objectMapper: ObjectMapper,
    private val authenticationSuccessHandler: AuthenticationSuccessHandler
) : AbstractAuthenticationProcessingFilter(
    AntPathRequestMatcher(loginPath, HttpMethod.POST.name)
) {

    @Throws(Exception::class)
    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val loginRequest: LoginRequest = this.objectMapper.readValue(request?.reader, LoginRequest::class.java)
        if (!loginRequest.validate()) {
            throw AuthenticationServiceException("유효하지 않는 아이디/비밀번호 입니다.")
        }

        return this._authenticate(loginRequest)
    }

    @Throws(Exception::class)
    override fun successfulAuthentication(
        request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?, authResult: Authentication?
    ) {
        this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult)
    }

    private fun _authenticate(loginRequest: LoginRequest): Authentication {
                                // 아이디와 패스워드로 인증하는 경우에 사용하는 토큰
        // username과 password를 조합해서 UsernamePasswordAuthenticationToken인스턴스를 만든다.
        val authenticationToken = UsernamePasswordAuthenticationToken(
            loginRequest.username,
            loginRequest.password
        )
        return this.authenticationManager.authenticate(authenticationToken)
    }
}
