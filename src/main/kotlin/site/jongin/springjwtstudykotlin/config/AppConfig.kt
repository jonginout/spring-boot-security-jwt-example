package site.jongin.springjwtstudykotlin.config

import kr.socar.alliance.server.properties.CorsProperties
import kr.socar.alliance.server.properties.TokenProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableConfigurationProperties(
    TokenProperties::class,
    CorsProperties::class
)
class AppConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
