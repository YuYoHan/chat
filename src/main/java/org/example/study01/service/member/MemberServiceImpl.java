package org.example.study01.service.member;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.study01.config.jwt.JwtProvider;
import org.example.study01.dto.jwt.TokenDTO;
import org.example.study01.dto.member.RequestMemberDTO;
import org.example.study01.dto.member.ResponseMemberDTO;
import org.example.study01.dto.member.Role;
import org.example.study01.dto.member.UpdateMemberDTO;
import org.example.study01.entity.jwt.TokenEntity;
import org.example.study01.entity.member.MemberEntity;
import org.example.study01.repository.jwt.TokenRepository;
import org.example.study01.repository.member.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;


    // 중복체크
    @Override
    public boolean emailCheck(String email) {
        MemberEntity findEmail = memberRepository.findByEmail(email);
        log.info("이메일 체크 : " + findEmail);
        if(findEmail == null) {
            return true;
        } else {
            return false;
        }
    }

    // 닉네임 체크
    @Override
    public boolean nickNameCheck(String nickName) {
        MemberEntity findNickName = memberRepository.findByNickName(nickName);
        log.info("닉네임 체크 : " + findNickName);
        if(findNickName == null) {
            return true;
        } else {
            return false;
        }
    }

    // 회원가입
    @Override
    public ResponseEntity<?> signUp(RequestMemberDTO memberDTO) {
        log.info("비번 : " + memberDTO.getMemberPw());
        String encodePw = passwordEncoder.encode(memberDTO.getMemberPw());
        log.info("암호화 : " + encodePw);
        try {
            log.info("email : " + memberDTO.getEmail());
            log.info("nickName : " + memberDTO.getNickName());

            // 이메일 중복 체크
            if (!emailCheck(memberDTO.getEmail())) {
                log.error("이미 존재하는 이메일이 있습니다.");
                return ResponseEntity.badRequest().body("이미 존재하는 이메일이 있습니다.");
            }

            // 닉네임 중복 체크
            if (!nickNameCheck(memberDTO.getNickName())) {
                log.error("이미 존재하는 닉네임이 있습니다.");
                return ResponseEntity.badRequest().body("이미 존재하는 닉네임이 있습니다.");
            }

            // 아이디가 없다면 DB에 등록해줍니다.
            MemberEntity member = MemberEntity.saveMember(memberDTO, encodePw);
            log.info("체크 : " + member);
            MemberEntity saveMember = memberRepository.save(member);
            log.info("member : " + saveMember);


            ResponseMemberDTO coverMember = ResponseMemberDTO.toMemberDTO(saveMember);
            return ResponseEntity.ok().body(coverMember);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // 회원 조회
    @Override
    public ResponseMemberDTO search(Long memberId) {
        try {
            MemberEntity findUser = memberRepository.findById(memberId)
                    .orElseThrow(EntityNotFoundException::new);
            return ResponseMemberDTO.toMemberDTO(findUser);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("회원이 존재 하지 않습니다.");
        }
    }

    // 회원 삭제
    @Override
    public String removeUser(Long memberId, String email) {
        // 회원 조회
        MemberEntity findUser = memberRepository.findByEmail(email);
        log.info("email check : " + email);
        log.info("email check2 : " + findUser.getEmail());

        // 회원이 비어있지 않고 넘어온 id가 DB에 등록된 id가 일치할 때
        if (findUser.getMemberId().equals(memberId)) {
            memberRepository.deleteByMemberId(memberId);
            return "회원 탈퇴 완료";
        } else {
            return "해당 유저가 아니라 삭제할 수 없습니다.";
        }
    }

    // 로그인
    @Override
    public ResponseEntity<?> login(String memberEmail, String memberPw) {
        try {
            // 회원 조회
            MemberEntity findUser = memberRepository.findByEmail(memberEmail);
            log.info("user : " + findUser);

            if (findUser != null) {
                // DB에 넣어져 있는 비밀번호는 암호화가 되어 있어서 비교하는 기능을 사용해야 합니다.
                // 사용자가 입력한 패스워드를 암호화하여 사용자 정보와 비교
                if (passwordEncoder.matches(memberPw, findUser.getMemberPw())) {
                    //  Spring Security의 인증 매커니즘을 통해 사용자를 인증하는데 사용
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(memberEmail, memberPw);
                    log.info("authentication : " + authentication);
                    List<GrantedAuthority> authoritiesForUser = getAuthoritiesForUser(findUser);

                    // JWT 생성
                    TokenDTO token = jwtProvider.createToken(authentication, authoritiesForUser, findUser.getNickName());
                    // 토큰 조회
                    TokenEntity findToken = tokenRepository.findByMemberEmail(token.getMemberEmail());

                    // 토큰이 없다면 새로 발급
                    if (findToken == null) {
                        log.info("발급한 토큰이 없습니다. 새로운 토큰을 발급합니다.");
                        // 토큰 생성과 조회한 memberId를 넘겨줌
                        findToken = TokenEntity.tokenEntity(token);
                        // 토큰id는 자동생성
                    } else {
                        log.info("이미 발급한 토큰이 있습니다. 토큰을 업데이트합니다.");
                        // 이미 존재하는 토큰이니 토큰id가 있다.
                        // 그 id로 토큰을 업데이트 시켜준다.
                        findToken.updateToken(token);
                    }
                    tokenRepository.save(findToken);
                    return ResponseEntity.ok().body(token);
                }
            }
            throw new EntityNotFoundException("회원이 존재하지 않습니다.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 회원의 권한을 GrantedAuthority타입으로 반환하는 메소드
    private List<GrantedAuthority> getAuthoritiesForUser(MemberEntity member) {
        Role memberRole = member.getRole();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + memberRole.name()));
        log.info("role : " + authorities);
        return authorities;
    }

    // 회원정보 수정
    @Override
    public ResponseEntity<?> updateUser(Long memberId, UpdateMemberDTO updateMemberDTO, String memberEmail) {
        try {
            // 회원조회
            MemberEntity findUser = memberRepository.findByEmail(memberEmail);
            log.info("user : " + findUser);

            // 닉네임 중복 체크
            if (!nickNameCheck(updateMemberDTO.getNickName())) {
                return ResponseEntity.badRequest().build();
            }

            String encodePw = null;
            if(updateMemberDTO.getMemberPw() != null) {
                encodePw = passwordEncoder.encode(updateMemberDTO.getMemberPw());
            }
            log.info("encodePw : " + encodePw);

            if (findUser.getMemberId().equals(memberId)) {
                findUser.updateMember(updateMemberDTO, encodePw);
                log.info("유저 수정 : " + findUser);

                MemberEntity updateUser = memberRepository.save(findUser);
                ResponseMemberDTO toResponseMemberDTO = ResponseMemberDTO.toMemberDTO(updateUser);
                return ResponseEntity.ok().body(toResponseMemberDTO);
            } else {
                return ResponseEntity.badRequest().build();
            }

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
