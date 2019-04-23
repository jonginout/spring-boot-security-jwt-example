package site.jongin.springjwtstudykotlin.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.jongin.springjwtstudykotlin.service.UserService

@RestController
@RequestMapping("/test")
class TestController(
    private val userService: UserService
) {

    @GetMapping
    @PreAuthorize("hasAuthority('TEST_READ')")
    fun index(): String {
        return "Authorized"
    }


    @GetMapping("/ok")
    fun test(): String {
        this.userService.findAll().forEach {
            println(it.toString())
        }
        return "OK"
    }
}
