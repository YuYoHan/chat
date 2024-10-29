package org.example.study01.document;

import lombok.*;
import org.example.study01.dto.ChatMessage;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "chat_messages") // MongoDB 컬렉션 지정
public class ChatMessageDocument {
    public enum MessageType {
        ENTER, TALK
    }

    @Id
    private String id;  // MongoDB에 자동으로 할당될 ID
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private LocalDateTime timestamp; // 메시지 생성 시간 저장

    public static ChatMessageDocument of(ChatMessage chatMessage) {
        return ChatMessageDocument.builder()
                .type(ChatMessageDocument.MessageType.TALK)
                .roomId(chatMessage.getRoomId())
                .sender(chatMessage.getSender())
                .message(chatMessage.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}