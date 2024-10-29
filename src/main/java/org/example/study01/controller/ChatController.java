package org.example.study01.controller;

import lombok.RequiredArgsConstructor;
import org.example.study01.dto.ChatMessage;
import org.example.study01.dto.ChatRoom;
import org.example.study01.service.ChatService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    // 새로운 채팅방 생성
    @PostMapping("/room")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ChatRoom createRoom(@RequestParam String name) {
        return chatService.createChatRoom(name);
    }

    // 채팅방 목록 조회
    @GetMapping("/rooms")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public List<ChatRoom> getChatRooms() {
        return chatService.getAllChatRooms();
    }

    // 채팅방의 모든 메시지 조회
    @GetMapping("/room/{roomId}/messages")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public List<ChatMessage> getMessages(@PathVariable String roomId) {
        return chatService.getMessagesByRoomId(roomId);
    }

    // 새로운 메시지 전송
    @PostMapping("/room/{roomId}/message")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public void sendMessage(@PathVariable String roomId, @RequestBody ChatMessage message) {
        chatService.saveAndSendMessage(message);
    }
}
