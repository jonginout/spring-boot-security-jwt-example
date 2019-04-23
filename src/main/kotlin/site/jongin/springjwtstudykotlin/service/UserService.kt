package site.jongin.springjwtstudykotlin.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import site.jongin.springjwtstudykotlin.domain.user.User
import site.jongin.springjwtstudykotlin.domain.user.UserPrincipal
import site.jongin.springjwtstudykotlin.domain.user.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {
    /*
    UserDetailsService 인터페이스는 DB에서 유저 정보를 가져오는 역할을 한다.
    해당 인터페이스의 메소드에서 DB의 유저 정보를 가져와서 AuthenticationProvider 인터페이스로 유저 정보를 리턴하면,
    그 곳에서 사용자가 입력한 정보와 DB에 있는 유저 정보를 비교한다.
     */

    // 이 메소드에서 유저 정보를 불러오는 작업을 하면 된다
    // Spring Security에서 사용자의 정보를 담는 인터페이스는 UserDetails 인터페이스이다
    @Transactional(readOnly = true)
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return UserPrincipal(
            this.userRepository.findByUsername(username).orElseThrow {
                UsernameNotFoundException("존재하지 않는 계정 : $username")
            }
        )
    }

    fun create(
        username: String,
        password: String,
        name: String
    ): User {
        return this.userRepository.save(
            User.newOf(
                username = username,
                rawPassword = password,
                passwordEncoder = passwordEncoder,
                name = name
            )
        )
    }

    fun findAll(): List<User> {
        return this.userRepository.findAll()
    }
}
