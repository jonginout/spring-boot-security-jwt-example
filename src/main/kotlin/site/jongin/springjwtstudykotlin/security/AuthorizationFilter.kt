package site.jongin.springjwtstudykotlin.security

import io.jsonwebtoken.JwtException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import site.jongin.springjwtstudykotlin.component.TokenUtils
import site.jongin.springjwtstudykotlin.domain.user.UserPrincipal
import site.jongin.springjwtstudykotlin.service.UserService
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthorizationFilter (
    private val tokenUtils: TokenUtils,
    private val userService: UserService
) : OncePerRequestFilter() {

    @Throws(Exception::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        logger.info("request url : ${request.requestURL}")

        val username: String? = this._getLoginIdFromHeader(request)

        if (username !== null){
            val userPrincipal = this.userService.loadUserByUsername(username) as UserPrincipal
            this._authenticate(request, userPrincipal)
        }

        filterChain.doFilter(request, response)
        // 필터 기능이 완료되고 다음 페이지로 연결
    }


    private fun _getLoginIdFromHeader(request: HttpServletRequest): String? {
        return try {
            val token = this.tokenUtils.extract(request).orElseThrow {
                JwtException("헤더값 에러")
            }

            this.tokenUtils.loginFromToken(token)
        } catch (e: Exception) {
            null
        }
    }

    private fun _authenticate(request: HttpServletRequest, userPrincipal: UserPrincipal) {
        val authenticationToken = UsernamePasswordAuthenticationToken(
            userPrincipal,
            null,
            setOf()
        ).apply {
                           // 새로운 인증 세부 사항 인스턴스를 만들 때 클래스에 의해 호출됩니다.
            this.details = WebAuthenticationDetailsSource().buildDetails(request)
        }

        SecurityContextHolder.getContext().authentication = authenticationToken
    }

}

/*
우리가 흔히 사용하는 아이디/패스워드 사용자 정보를 넣고 실제 가입된 사용자인지 체크한 후 인증에 성공하면
사용자의 principal(아이디)과 credential(비밀번호) 정보를 Authentication에 담습니다.

spring security에서 방금 담은 Authentication을 SecurityContext에 보관한다.
이 SecurityContext를 SecurityContextHolder에 담아 보관한다.

 */
