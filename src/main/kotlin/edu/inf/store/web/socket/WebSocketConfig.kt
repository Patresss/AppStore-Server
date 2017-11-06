package edu.inf.store.web.socket

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry


@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : AbstractWebSocketMessageBrokerConfigurer() {

    companion object {
        val log: Logger = LoggerFactory.getLogger(WebSocketConfig::class.java)
    }

    override fun configureMessageBroker(config: MessageBrokerRegistry?) {
        log.warn("Configure message broker: $config")
        config?.enableSimpleBroker("/topic")
        config?.setApplicationDestinationPrefixes("/app")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        log.debug("RegisterStompEndpoints: $registry")
        registry.addEndpoint("/chat").setAllowedOrigins("*")
        registry.addEndpoint("/chat").setAllowedOrigins("*").withSockJS()
    }

}
