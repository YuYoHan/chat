package org.example.study01.config.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.example.study01.dto.member.Role;
import org.example.study01.entity.member.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Setter
@Getter
@ToString
@Log4j2
@Component
@NoArgsConstructor
public class PrincipalDetails implements UserDetails{

    // 일반 로그인 정보를 저장하기 위한 필드
    private MemberEntity member;
    // OAuth2 로그인 정보를 저장하기 위한 필드
    // 일반적으로 attributes에는 사용자의 아이디(ID), 이름, 이메일 주소, 프로필 사진 URL 등의 정보가 포함됩니다.
    private Map<String, Object> attributes;


    // 일반 로그인
    public PrincipalDetails(MemberEntity member) {
        this.member = member;
    }

    // OAuth2 로그인
    public PrincipalDetails(MemberEntity member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }



    // 해당 유저의 권한을 권한을 리턴
    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> collection = new ArrayList<>();
        Role memberRole = member.getRole();
        collection.add(new SimpleGrantedAuthority("ROLE_" + memberRole.name()));
        return collection;
    }

    // 사용자 패스워드를 반환
    @Override
    public String getPassword() {
        return member.getMemberPw();
    }

    // 사용자 이름 반환
    @Override
    public String getUsername() {
        return member.getEmail();
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        // true = 만료되지 않음
        return true;
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        // true = 잠금되지 않음
        return true;
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        // true = 만료되지 않음
        return true;
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        // true = 사용 가능
        return true;
    }

}
