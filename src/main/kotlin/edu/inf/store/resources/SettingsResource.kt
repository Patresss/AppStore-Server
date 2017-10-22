package edu.inf.store.resources

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
open class SettingsResource() {

    @GetMapping("/settings")
    open fun getAllGames(): String {
        return "dziaa"
    }

}