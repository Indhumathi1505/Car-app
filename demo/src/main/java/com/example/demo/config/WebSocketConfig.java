package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // /topic = server broadcasts messages here
        // /app = client sends messages here
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")  // must match React SOCKET_URL
                .setAllowedOriginPatterns("http://localhost:5173", "https://car-app-ch3s.onrender.com") // frontend origin
                .withSockJS()
                .setInterceptors(new HttpSessionHandshakeInterceptor()); // ✅ preserves session cookies
    }
}
