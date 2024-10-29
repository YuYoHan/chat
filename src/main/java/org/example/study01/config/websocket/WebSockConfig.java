package org.example.study01.config.websocket;

import lombok.RequiredArgsConstructor;
import org.example.study01.config.stomp.StompHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메세지를 받을 때, 경로를 설정해주는 함수
        // 스프링에서 제공해주는 내장 브로커를 사용하는 함수
        // “/topic”가 api에 prefix로 붙은 경우, messageBroker가 해당 경로를 가로챈다.
        config.enableSimpleBroker("/topic"); // 구독 주제 경로
        // 메세지를 보낼 때, 관련 경로를 설정해주는 함수
        // 클라이언트가 메세지를 보낼 떄, 경로 앞에 “/app”이 붙어있으면 Broker로 보내진다.
        config.setApplicationDestinationPrefixes("/send"); // 클라이언트에서 메시지 전송 경로
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 소켓 연결 uri이다. 소켓을 연결할 때에는 다음과 같은 통신이 이루어진다.
        registry.addEndpoint("/ws/chat")
                .setAllowedOrigins("http://localhost:8080")
                .withSockJS(); // SockJS를 통해 연결 지원
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }

}
