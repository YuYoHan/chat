package org.example.study01.dto.member;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class UpdateMemberDTO {
    private String nickName;
    private String memberPw;


    @Builder
    public UpdateMemberDTO(String nickName, String memberPw) {
        this.nickName = nickName;
        this.memberPw = memberPw;
    }
}
