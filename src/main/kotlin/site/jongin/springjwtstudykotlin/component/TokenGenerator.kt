package site.jongin.springjwtstudykotlin.component

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import kr.socar.alliance.server.properties.TokenProperties
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
class TokenGenerator(
    private val tokenProperties: TokenProperties
) {
    private val expiration: Date
        get() = Date.from(
            LocalDateTime.now()
                .plusSeconds(tokenProperties.expiration!!)
                .atZone(ZoneId.systemDefault())
                .toInstant()
        )

    fun generateToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(this.expiration)
            .signWith(TOKEN_ALGORITHM, this.tokenProperties.secret)
            .compact()
    }

    companion object {
        private val TOKEN_ALGORITHM = SignatureAlgorithm.HS512
    }
}
