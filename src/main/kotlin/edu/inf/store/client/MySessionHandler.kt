package edu.inf.store.client

import edu.inf.store.domain.OutputMessage
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter

import java.lang.reflect.Type

class MySessionHandler : StompSessionHandlerAdapter() {

    override fun afterConnected(session: StompSession?, connectedHeaders: StompHeaders?) {
        session?.subscribe("/topic/messages", this)
        session?.send("/app/chat", "{\"name\":\"Patryk\", \"text\":\"Hello\"}".toByteArray())
        println("New session:: ${session?.sessionId}")
    }

    override fun handleException(session: StompSession?, command: StompCommand?, headers: StompHeaders?, payload: ByteArray?, exception: Throwable?) {
        exception?.printStackTrace()
    }

    override fun getPayloadType(headers: StompHeaders?): Type {
        return OutputMessage::class.java
    }

    override fun handleFrame(headers: StompHeaders?, payload: Any?) {
        println("Received: $payload")
    }
}