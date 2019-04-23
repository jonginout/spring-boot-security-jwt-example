package site.jongin.springjwtstudykotlin.domain.user

import org.springframework.security.core.authority.SimpleGrantedAuthority

/*
    principal : 보호된 대상에 접근하는 유저
 */
class UserPrincipal(
    val user: User
) : org.springframework.security.core.userdetails.User(
    user.username,
    user.password,
    user.authorities.map {
        SimpleGrantedAuthority(it.authority)
    }
)
