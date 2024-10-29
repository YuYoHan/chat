package org.example.study01.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ChatMessage {
    public enum MessageType{
        ENTER, TALK
    }

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;

    public void change(String roomId) {
        this.roomId = roomId;
    }
}
