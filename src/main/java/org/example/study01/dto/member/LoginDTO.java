package org.example.study01.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.antlr.v4.runtime.misc.NotNull;

/*
 *   writer : 유요한
 *   work :
 *          로그인할 때 받는 RequestDTO
 *   date : 2023/10/10
 * */
@ToString
@Getter
@NoArgsConstructor
public class LoginDTO {
    private String memberEmail;
    private String memberPw;

    @Builder
    public LoginDTO(String memberEmail, String memberPw) {
        this.memberEmail = memberEmail;
        this.memberPw = memberPw;
    }
}
