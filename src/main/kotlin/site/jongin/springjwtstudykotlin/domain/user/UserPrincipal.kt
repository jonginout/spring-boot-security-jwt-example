package site.jongin.springjwtstudykotlin.domain.user

class UserPrincipal(
    val user: User
) : org.springframework.security.core.userdetails.User(
    user.username,
    user.password,
    setOf() // java의 set을 만드는 것
)
