package kr.socar.alliance.server.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "token")
class TokenProperties {
    lateinit var secret: String
    lateinit var headerName: String
    lateinit var headerPrefix: String
    var expiration: Long? = 0

    override fun toString(): String {
        return "TokenProperties(secret='$secret', headerName='$headerName', headerPrefix='$headerPrefix', expiration=$expiration)"
    }
}
