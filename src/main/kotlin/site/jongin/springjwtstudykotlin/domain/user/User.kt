package site.jongin.springjwtstudykotlin.domain.user

import org.springframework.security.crypto.password.PasswordEncoder
import site.jongin.springjwtstudykotlin.domain.authorities.Authority
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
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

    @ManyToMany
    @JoinTable(
        name = "users_authorities",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "authority_id")]
    )
    var authorities: Collection<Authority> = emptyList()

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

    override fun toString(): String {
        return "User(id=$id, username='$username', password='$password', name='$name', authorities=$authorities)"
    }


}
