package site.jongin.springjwtstudykotlin.security

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class UnauthorizedAccessDeniedHandler: AccessDeniedHandler {

    @Throws(Exception::class)
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, accessDeniedException.message)
    }

}
