package site.jongin.springjwtstudykotlin.component

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import kr.socar.alliance.server.properties.TokenProperties
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import site.jongin.springjwtstudykotlin.exception.AuthorizationHeaderException
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class TokenUtils(
    private val tokenProperties: TokenProperties
) {

    fun extract(header: String): String {
        if (StringUtils.isEmpty(header)) {
            throw AuthorizationHeaderException("Authorization header is empty.")
        }

        if (this._validateHeaderLength(header)) {
            throw AuthorizationHeaderException("Authorization header length is short.")
        }

        return header.substring(this.tokenProperties.headerPrefix.length)
    }

    fun usernameFromToken(token: String): String {
                                        // Claims 중 subject
        return _getClaimFromToken(token, Claims::getSubject)
    }

    private fun _validateHeaderLength(tokenHeader: String): Boolean {
        return tokenHeader <= this.tokenProperties.headerPrefix
    }

    // <T> : 제너릭 타입이 필수적으로 선언되어야 한다라는 것           // claim : jwt에서 정보의 한 조각
                                                            // 람다(함수) 자체가 파라미터로 들어감
    private fun <T> _getClaimFromToken(token: String, resolver: (claims: Claims) -> T): T {
        // 람다 함수 호출
        return resolver(this._parse(token))
    }
    // private <제네릭> 함수명(): 리턴값 {}

    @Throws(JwtException::class)
    private fun _parse(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(this.tokenProperties.secret)
            .parseClaimsJws(token)
            .body
    }
}
