package org.example.study01.service.member;

import org.example.study01.dto.member.RequestMemberDTO;
import org.example.study01.dto.member.ResponseMemberDTO;
import org.example.study01.dto.member.UpdateMemberDTO;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    // 회원가입
    ResponseEntity<?> signUp(RequestMemberDTO requestMemberDTO);
    // 회원조회
    ResponseMemberDTO search(Long memberId);
    // 회원삭제
    String removeUser(Long memberId, String email);
    // 로그인
    ResponseEntity<?> login(String memberEmail, String memberPw);
    // 회원수정
    ResponseEntity<?> updateUser(Long memberId, UpdateMemberDTO updateMemberDTO, String memberEmail);
    // 이메일 체크
    boolean emailCheck(String email);
    // 닉네임 체크
    boolean nickNameCheck(String nickName);
}
