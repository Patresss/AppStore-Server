package edu.inf.store.web.socket

import edu.inf.store.domain.Greeting
import edu.inf.store.domain.HelloMessage
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller


@Controller
class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    @Throws(Exception::class)
    fun greeting(message: HelloMessage): Greeting {
        Thread.sleep(1000) // simulated delay
        return Greeting("Hello, " + message.name + "!")
    }

}