package site.jongin.springjwtstudykotlin.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import site.jongin.springjwtstudykotlin.component.TokenGenerator
import site.jongin.springjwtstudykotlin.domain.user.UserPrincipal
import site.jongin.springjwtstudykotlin.dto.response.TokenResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoginAuthenticationSuccessHandler(
    private val tokenGenerator: TokenGenerator,
    private val objectMapper: ObjectMapper
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val alliancePrincipal = authentication.principal as UserPrincipal

        val tokenResponse = TokenResponse(
            token = this.tokenGenerator.generateToken(alliancePrincipal.username),
            type = "Bearer"
        )

        response.status = HttpServletResponse.SC_OK
        response.contentType = MediaType.APPLICATION_JSON_UTF8_VALUE

        this.objectMapper.writeValue(response.writer, tokenResponse)
    }
}
