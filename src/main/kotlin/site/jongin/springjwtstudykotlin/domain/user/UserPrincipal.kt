package site.jongin.springjwtstudykotlin.domain.user

/*
    principal : 보호된 대상에 접근하는 유저
 */
class UserPrincipal(
    val user: User
) : org.springframework.security.core.userdetails.User(
    user.username,
    user.password,
    setOf() // java의 set을 만드는 것
)
