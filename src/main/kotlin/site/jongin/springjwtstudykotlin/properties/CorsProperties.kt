package kr.socar.alliance.server.properties

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "cors")
class CorsProperties {
    lateinit var origins: List<String>
    lateinit var methods: List<String>
    lateinit var headers: List<String>

    override fun toString(): String {
        return "CorsProperties(origins=$origins, methods=$methods, headers=$headers)"
    }
}
