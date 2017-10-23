package edu.inf.store.web.socket

import edu.inf.store.domain.OutputMessage
import edu.inf.store.domain.Message
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller


@Controller
class ChatController {

    companion object {
        val log: Logger = LoggerFactory.getLogger(ChatController::class.java)
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    @Throws(Exception::class)
    fun send(message: Message): OutputMessage {
        log.debug("Sending message")
        return OutputMessage(from = message.name, text = message.text)
    }

}