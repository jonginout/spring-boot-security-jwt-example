package site.jongin.springjwtstudykotlin.domain.authorities

import org.springframework.data.jpa.repository.JpaRepository

interface AuthorityRepository : JpaRepository<Authority, Long> {

}
