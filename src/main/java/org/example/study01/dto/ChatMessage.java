package org.example.study01.dto;

import lombok.*;
import org.example.study01.entity.member.MemberEntity;

import java.time.LocalDateTime;

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
    @Setter
    private String message;
    private Long userId;
    private LocalDateTime time;

    public void setUser(MemberEntity user) {
        this.userId = user.getMemberId();
        this.sender = user.getNickName();
        this.type = MessageType.ENTER;
        this.time = LocalDateTime.now();
    }
}
