package org.example.study01.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.antlr.v4.runtime.misc.NotNull;
import org.example.study01.entity.member.MemberEntity;

@ToString
@Getter
@NoArgsConstructor
public class ResponseMemberDTO {
    private Long memberId;
    private String email;
    private String memberName;
    private String nickName;
    private String memberPw;

    @Builder
    public ResponseMemberDTO(Long memberId,
                             String email,
                             String memberName,
                             String nickName,
                             String memberPw
                            ) {
        this.memberId = memberId;
        this.email = email;
        this.memberName = memberName;
        this.nickName = nickName;
        this.memberPw = memberPw;
    }

    // 엔티티를 DTO로 반환
    public static ResponseMemberDTO toMemberDTO(MemberEntity member) {
        return ResponseMemberDTO.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .memberPw(member.getMemberPw())
                .nickName(member.getNickName())
                .memberName(member.getMemberName())
                .build();
    }

}
