package org.example.study01.entity.member;

import jakarta.persistence.*;
import lombok.*;
import org.example.study01.dto.member.RequestMemberDTO;
import org.example.study01.dto.member.Role;
import org.example.study01.dto.member.UpdateMemberDTO;


@Entity(name = "member")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    @Column(name = "member_email", nullable = false)
    private String email;

    @Column(name = "member_pw")
    private String memberPw;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public MemberEntity(Long memberId,
                        String memberName,
                        String email,
                        String memberPw,
                        String nickName,
                        Role role) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.email = email;
        this.memberPw = memberPw;
        this.nickName = nickName;
        this.role = role;
    }

    // 저장
    public static MemberEntity saveMember(RequestMemberDTO member, String password) {
        return MemberEntity.builder()
                .email(member.getEmail())
                .memberPw(password)
                .memberName(member.getMemberName())
                .nickName(member.getNickName())
                .role(Role.USER)
                .build();
    }

    public void updateMember(UpdateMemberDTO updateMember, String encodePw) {
        this.memberPw = updateMember.getMemberPw() == null ? this.memberPw : encodePw;
        this.nickName = updateMember.getNickName() == null ? this.nickName : updateMember.getNickName();

    }


}
