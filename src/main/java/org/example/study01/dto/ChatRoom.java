package org.example.study01.dto;

import lombok.*;
import org.example.study01.entity.ChatRoomEntity;
import org.example.study01.service.ChatService;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatRoom {
    private Long roomId;
    private String name;

    public static ChatRoom of(ChatRoomEntity chatRoom) {
        return ChatRoom.builder()
                .roomId(chatRoom.getRoomId())
                .name(chatRoom.getName())
                .build();
    }

}
