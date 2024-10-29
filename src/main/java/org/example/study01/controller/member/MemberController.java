package org.example.study01.controller.member;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.study01.dto.member.LoginDTO;
import org.example.study01.dto.member.RequestMemberDTO;
import org.example.study01.dto.member.ResponseMemberDTO;
import org.example.study01.dto.member.UpdateMemberDTO;
import org.example.study01.service.member.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/*
 *   writer : YuYoHan
 *   work :
 *          유저의 CRUD 기능과 이메일 조회와 닉네임 조회 기능이 있고
 *          주문 조회와 나의 문의글을 보는 기능이 있습니다.
 *   date : 2024/01/22
 * */
@RestController
// @Slf4j를 사용하지 않고 Log4j2를 사용하는 이유는
// 기능면에서 더 좋기 때문입니다.
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("")
    // BindingResult 타입의 매개변수를 지정하면 BindingResult 매개 변수가 입력값 검증 예외를 처리한다.
    public ResponseEntity<?> join(@Validated @RequestBody RequestMemberDTO member,
                                  BindingResult result) {
        try {
            // 입력값 검증 예외가 발생하면 예외 메시지를 응답한다.
            if (result.hasErrors()) {
                log.info("BindingResult error : " + result.hasErrors());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getClass().getSimpleName());
            }

            ResponseEntity<?> signUp = memberService.signUp(member);
            return ResponseEntity.ok().body(signUp);
        } catch (Exception e) {
            log.error("예외 : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 회원 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<?> search(@PathVariable Long memberId) {
        try {
            ResponseMemberDTO search = memberService.search(memberId);
            return ResponseEntity.ok().body(search);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 회원 탈퇴
    @DeleteMapping("/{memberId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String remove(@PathVariable Long memberId,
                         @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            log.info("email : " + email);
            String remove = memberService.removeUser(memberId, email);
            return remove;
        } catch (Exception e) {
            return "회원탈퇴 실패했습니다. :" + e.getMessage();
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            String email = loginDTO.getMemberEmail();
            String password = loginDTO.getMemberPw();
            ResponseEntity<?> login = memberService.login(email, password);
            log.info("토큰 : " + login.getBody());
            return ResponseEntity.ok().body(login);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인을 실패했습니다.");
        }
    }

    // 회원 수정
    @PutMapping("/{memberId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long memberId,
                                    @Validated @RequestBody UpdateMemberDTO updateMemberDTO,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            log.info("email : " + email);
            log.info("수정 정보 체크 : " + updateMemberDTO);
            ResponseEntity<?> responseEntity = memberService.updateUser(memberId, updateMemberDTO, email);
            return ResponseEntity.ok().body(responseEntity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // 중복체크
    @GetMapping("/email/{memberEmail}")
    public boolean emailCheck(@PathVariable String memberEmail) {
        log.info("email : " + memberEmail);
       return memberService.emailCheck(memberEmail);
    }

    // 닉네임 조회
    @GetMapping("/nickName/{nickName}")
    public boolean nickNameCheck(@PathVariable String nickName) {
        log.info("nickName : " + nickName);
        return memberService.nickNameCheck(nickName);
    }



}
