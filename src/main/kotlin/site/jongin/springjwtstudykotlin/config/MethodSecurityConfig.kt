package site.jongin.springjwtstudykotlin.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration
import site.jongin.springjwtstudykotlin.security.CustomPermissionEvaluator

/*
    스프링 시큐리티 2.0 에서부터 서비스 계층의 메소드에 보안을 추가할 수 있도록 지원한다.
    스프링 시큐리티 3.0에서는 표현 기반의 어노테이션을 사용할 수 있게 된다.

    우리는 Configuration 클래스에 @EnableGlobalMethodSecurity를 적용함으로써 어노테이션 기반의 보안을 활성화시킬 수 있습니다.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class MethodSecurityConfig : GlobalMethodSecurityConfiguration() {

    override fun createExpressionHandler(): MethodSecurityExpressionHandler {
        return DefaultMethodSecurityExpressionHandler().apply {
            this.setPermissionEvaluator(CustomPermissionEvaluator())
            /*
                PermissionEvaluator을 사용할 수 있게 된다.
             */
        }
    }
}
