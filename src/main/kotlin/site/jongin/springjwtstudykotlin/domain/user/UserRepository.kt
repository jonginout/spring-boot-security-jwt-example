package site.jongin.springjwtstudykotlin.domain.user

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long> {

    @EntityGraph(attributePaths = ["authorities"])
    fun findByUsername(username: String): Optional<User>
}

/*
    EntityGraph를 사용하지 않는다면 조인에 대한 쿼리
    하위 엔티티들을 첫 쿼리 실행시 한번에 가져오지 않고,
    Lazy Loading으로 필요한 곳에서 사용되어 쿼리가 실행될때 발생하는 문제가 N+1 쿼리 문제입니다.

    조회 결과가 10만개면 어떻게 될까요?
    한번의 서비스 로직 실행에서 DB 조회가 10만번 일어난다

    @EntityGraph의 attributePaths에 쿼리 수행시 바로 가져올 필드명을 지정하면 Lazy가 아닌 Eager 조회로 가져오게 됩니다.

    그럼 왜 엔티티에 직접 Eager를 안하냐?
    -> 이 findByUsername 쿼리에만 Eager조회를 하고 싶으니깐
 */
