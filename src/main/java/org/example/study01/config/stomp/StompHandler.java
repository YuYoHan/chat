package org.example.study01.config.stomp;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.study01.config.jwt.JwtProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Log4j2
public class StompHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // Check for the CONNECT command
        if (accessor.getCommand() == StompCommand.CONNECT) {
            // Get the token from the Authorization header
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            log.info("authHeader: " + authHeader);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // Extract the token
                log.info("token: " + token);
                if (!jwtProvider.validateToken(token)) {
                    throw new AccessDeniedException("Invalid JWT token");
                }
            } else {
                throw new AccessDeniedException("Authorization header not found");
            }
        }
        return message;
    }
}
