package org.example.study01.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.example.study01.dto.ChatMessage;
import org.example.study01.service.ChatService;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class ChatRoomEntity {
    @Id @GeneratedValue
    private Long roomId;
    private String name;
}
