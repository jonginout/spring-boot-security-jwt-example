package site.jongin.springjwtstudykotlin.domain.user

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    @EntityGraph(attributePaths = ["authorities"])
    fun findByUsername(username: String): Optional<User>
}
