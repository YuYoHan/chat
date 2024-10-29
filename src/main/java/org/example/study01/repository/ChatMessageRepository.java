package org.example.study01.repository;

import org.example.study01.document.ChatMessageDocument;
import org.example.study01.dto.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessageDocument, String> {
    // 특정 채팅방의 메시지 조회 메서드
    List<ChatMessage> findByRoomId(String roomId);
}
