package site.jongin.springjwtstudykotlin.domain.user

import org.springframework.security.crypto.password.PasswordEncoder
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
class User private constructor() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(unique = true)
    var username: String = ""
    var password: String = ""
    var name: String = ""

    companion object {
        fun empty() = User()

        fun newOf(
            username: String,
            rawPassword: String,
            name: String,
            passwordEncoder: PasswordEncoder
        ): User {
            return User().apply {
                this.username = username
                this.password = passwordEncoder.encode(rawPassword)
                this.name = name
            }
        }
    }
}
