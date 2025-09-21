package com.app.chatlinks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS().setSessionCookieNeeded(false).setDisconnectDelay(5000);

    }
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(2048 * 2048);
        registry.setSendBufferSizeLimit(2048 * 2048);
        registry.setSendTimeLimit(2048 * 2048);
    }
    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(2048 * 2048);
        container.setMaxSessionIdleTimeout(2048L * 2048L);
        container.setAsyncSendTimeout(2048L * 2048L);
        container.setMaxBinaryMessageBufferSize(2048 * 2048);
        return container;
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //use this if you dont have rabbitmq
        config.enableSimpleBroker("/topic/", "/queue/", "/user/");
        config.setApplicationDestinationPrefixes("/app");

        // use this if you have rabbitmq
//        config.enableStompBrokerRelay("/topic/", "/queue/").setRelayHost("localhost").setRelayPort(61613).setClientLogin("test")
//                .setClientPasscode("test");
//        config.setApplicationDestinationPrefixes("/app");
    }
}