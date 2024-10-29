package org.example.study01.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@ToString
@NoArgsConstructor
public class RequestMemberDTO {
    private String email;
    private String memberName;
    private String nickName;
    private String memberPw;
    @Builder
    public RequestMemberDTO(String email,
                            String memberName,
                            String nickName,
                            String memberPw) {
        this.email = email;
        this.memberName = memberName;
        this.nickName = nickName;
        this.memberPw = memberPw;
    }
}
