package site.jongin.springjwtstudykotlin.config

import com.fasterxml.jackson.databind.ObjectMapper
import kr.socar.alliance.server.properties.CorsProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import site.jongin.springjwtstudykotlin.security.AuthorizationFilter
import site.jongin.springjwtstudykotlin.security.LoginAuthenticationProcessingFilter
import site.jongin.springjwtstudykotlin.security.UnauthorizedAuthenticationEntryPoint
import java.lang.Exception

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authorizationFilter: AuthorizationFilter,
    private val authenticationSuccessHandler: AuthenticationSuccessHandler,
    private val objectMapper: ObjectMapper
) : WebSecurityConfigurerAdapter() {

//    @Bean
//    @Throws(Exception::class)
//    override fun authenticationManagerBean(): AuthenticationManager {
//        return super.authenticationManagerBean()
//    }

    @Bean
    fun corsConfigurationSource(
        corsProperties: CorsProperties
    ): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            this.allowedOrigins = corsProperties.origins
            this.allowedMethods = corsProperties.methods
            this.allowedHeaders = corsProperties.headers
        }
        val source = UrlBasedCorsConfigurationSource()

        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .cors().and()
            .csrf().disable()
            // ErrorHandling에 인증되지 않았을 때의 동작을 AuthenticationEntryPoint를 설정하여 제어할 수 있다.
            .exceptionHandling().authenticationEntryPoint(UnauthorizedAuthenticationEntryPoint()).and()
            // 사용자의 쿠키에 세션을 저장하지 않겠다는 옵션입니다
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

        http
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/health").permitAll()
            .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .anyRequest().authenticated()   // 인증된 사용자만 접근할 수 있습니다.

        /*
        spring web security의 기본 필터인 UsernamePasswordAuthenticationFilter::class.java의 이전에 실행하겠다.
        그런데 내가 정의한 LoginAuthenticationProcessingFilter필터에서 이미 인증하고 넘어가기 때문에
        UsernamePasswordAuthenticationFilter 필터는 실행될 일이 없다

        다만 UsernamePasswordAuthenticationFilter와 비슷한 동작처리를 해야기 때문에
        AbstractAuthenticationProcessingFilter를 상속받는다.
         */
        http.addFilterBefore(this.loginProcessingFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(this.authorizationFilter, UsernamePasswordAuthenticationFilter::class.java)
    }


    private fun loginProcessingFilter(): LoginAuthenticationProcessingFilter {
        val authenticationManager = this.authenticationManagerBean()

        return LoginAuthenticationProcessingFilter(
            LOGIN_PATH,
            this.objectMapper,
            this.authenticationSuccessHandler
        ).apply {
            this.setAuthenticationManager(authenticationManager)
        }
    }

    companion object {
        private const val LOGIN_PATH = "/auth/login"
    }
}
