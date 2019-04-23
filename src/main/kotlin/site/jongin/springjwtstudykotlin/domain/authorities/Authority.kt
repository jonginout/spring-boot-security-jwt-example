package site.jongin.springjwtstudykotlin.domain.authorities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "authorities")
class Authority private constructor() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    var authority: String? = ""

    override fun toString(): String {
        return "Authority(id=$id, authority=$authority)"
    }

}
