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
import site.jongin.springjwtstudykotlin.security.LoginAuthenticationSuccessHandler
import site.jongin.springjwtstudykotlin.security.UnauthorizedAccessDeniedHandler
import site.jongin.springjwtstudykotlin.security.UnauthorizedAuthenticationEntryPoint
import java.lang.Exception

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authorizationFilter: AuthorizationFilter,   // 토큰인증
    private val authenticationSuccessHandler: LoginAuthenticationSuccessHandler, // username/password (로그인화면) 인증
    private val objectMapper: ObjectMapper
) : WebSecurityConfigurerAdapter() {

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

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
            .exceptionHandling().authenticationEntryPoint(UnauthorizedAuthenticationEntryPoint()).and()
            .exceptionHandling().accessDeniedHandler(UnauthorizedAccessDeniedHandler()).and()
            /*
                인증이 되지 않았을 경우(비로그인)에는 AuthenticationEntryPoint 부분에서 AuthenticationException 을 발생 시키고,
                인증(로그인)은 되었으나 해당 요청에 대한 권한이 없을 경우에는 AccessDeniedHandler 부분에서 AccessDeniedException 이 발생된다.
             */
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() // 사용자의 쿠키에 세션을 저장하지 않겠다는 옵션입니다 (무상태성)

        http
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/health").permitAll()
            .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .anyRequest().authenticated()   // 인증된 사용자만 접근할 수 있습니다.

            //.antMatchers("/admin/**").hasRole("ADMIN")
            //.antMatchers("/admin/**").authenticated() /admin 경로에는 로그인 시에만 접근이 가능하도록 막히게 된다.

        /*
            spring web security의 기본 필터인 UsernamePasswordAuthenticationFilter::class.java의 이전에 실행하겠다.
            그런데 내가 정의한 LoginAuthenticationProcessingFilter필터에서 이미 인증하고 넘어가기 때문에
            UsernamePasswordAuthenticationFilter 필터는 실행될 일이 없다

            다만 UsernamePasswordAuthenticationFilter와 비슷한 동작처리를 해야기 때문에
            AbstractAuthenticationProcessingFilter를 상속받는다.
         */
        // UsernamePasswordAuthenticationFilter필터 이전에 loginProcessingFilter를 실행하겠다는 뜻
        http.addFilterBefore(this.loginProcessingFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(this.authorizationFilter, UsernamePasswordAuthenticationFilter::class.java)
    }


    private fun loginProcessingFilter(): LoginAuthenticationProcessingFilter {
        /*
            먼저 Spring Security에서 유저 인증을 구현할 때 최상위 인터페이스로는
            AuthenticationManager Interface가 존재하고 이를 통해 유저 인증 구현 한다.
         */
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
