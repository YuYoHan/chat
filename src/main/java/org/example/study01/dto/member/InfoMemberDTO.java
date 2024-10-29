package org.example.study01.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.study01.entity.member.MemberEntity;

/*
 *   writer : 오현진
 *   work :
 *          유저에 대한 필요한 정보를 보내주는 RequestDTO
 *   date : 2023/11/16
 * */
@ToString
@Getter
@NoArgsConstructor
public class InfoMemberDTO {

    private Long id;
    private String nickName;
    private String email;

    @Builder
    public InfoMemberDTO(Long id, String nickName, String email) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
    }

    public static InfoMemberDTO toInfoMember(ResponseMemberDTO member){
        return InfoMemberDTO.builder()
                .id(member.getMemberId())
                .nickName(member.getNickName())
                .email(member.getEmail())
                .build();
    }

    public static InfoMemberDTO from(MemberEntity member){
        return InfoMemberDTO.builder()
                .id(member.getMemberId())
                .nickName(member.getNickName())
                .email(member.getEmail())
                .build();
    }
}
