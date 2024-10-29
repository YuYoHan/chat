package org.example.study01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.study01.dto.ChatMessage;
import org.example.study01.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Log4j2
@RequiredArgsConstructor
@Controller
public class ChatStompController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/api/chat/sendMessage")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public void sendMessage(@Payload ChatMessage message) {
        log.info("------------------------------------------------------");
        log.info("message: " + message);
        chatService.saveAndSendMessage(message);  // MongoDB에 메시지 저장
        messagingTemplate.convertAndSend("/topic/chat/" + message.getRoomId(), message);  // 특정 채팅방 주제로 메시지 전송
    }
}