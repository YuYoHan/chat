package org.example.study01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.study01.dto.ChatMessage;
import org.example.study01.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
public class MessageController {
    private final ChatService chatService;

    //채팅 메세지 보내기
    @MessageMapping("/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage enter(ChatMessage message,
                      @DestinationVariable int roomId) {
        chatService.saveAndSendMessage(message);
        return message;
    }
}