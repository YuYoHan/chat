package org.example.study01.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.study01.document.ChatMessageDocument;
import org.example.study01.dto.ChatMessage;
import org.example.study01.dto.ChatRoom;
import org.example.study01.entity.ChatRoomEntity;
import org.example.study01.repository.ChatMessageRepository;
import org.example.study01.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final NotificationService notificationService;

    // 채팅 메시지 저장 및 전송
    public void saveAndSendMessage(ChatMessage message) {
        ChatMessageDocument chatMessageDocument = ChatMessageDocument.of(message);
        chatMessageRepository.save(chatMessageDocument);  // MongoDB에 메시지 저장
        // 알림 전송
        notificationService.sendNotification(message.getSender(), "새 메시지가 도착했습니다!");
    }

    // 특정 채팅방의 모든 메시지 조회
    public List<ChatMessage> getMessagesByRoomId(String memberId) {
        return chatMessageRepository.findByRoomId(memberId);
    }


    // 채팅방 생성
    public ChatRoom createChatRoom(String name) {
        ChatRoomEntity chatRoom = ChatRoomEntity.builder()
                .name(name)
                .build();
        ChatRoom response = ChatRoom.of(chatRoomRepository.save(chatRoom));// MongoDB에 채팅방 저장
        log.info("채팅방 반환값 : " + response);
        return response;
    }

    public List<ChatRoom> getAllChatRooms() {
        List<ChatRoomEntity> all = chatRoomRepository.findAll();

        List<ChatRoom> collect = all.stream()
                .map(ChatRoom::of)
                .collect(Collectors.toList());

        log.info("채팅방 {} ", collect );
        return collect;
    }
}
