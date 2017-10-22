package edu.inf.store.web.rest.resources

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class WelcomeResource {

    @GetMapping("/welcome")
    fun welcome(): String {
        return "Welcome!"
    }

    @GetMapping("/welcome/{name}")
    fun welcome(@PathVariable name: String): String {
        return "Welcome $name!"
    }


}