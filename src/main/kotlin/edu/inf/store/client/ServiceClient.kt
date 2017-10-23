package edu.inf.store.client

import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.util.*

class ServiceClient

fun main2(args: Array<String>) {
    val webSocketClient = StandardWebSocketClient()
    val stompClient = WebSocketStompClient(webSocketClient)
    stompClient.messageConverter = MappingJackson2MessageConverter()
    stompClient.taskScheduler = ConcurrentTaskScheduler()

    val url = "ws://127.0.0.1:8080/chat"
    val sessionHandler = MySessionHandler()
    stompClient.connect(url, sessionHandler)

    Scanner(System.`in`).nextLine() //Don't close immediately.
}

