package org.example.study01.entity.jwt;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.study01.dto.jwt.TokenDTO;

import java.util.Date;

/*
 *   writer : 유요한
 *   work :
 *          JWT 엔티티
 *   date : 2023/11/15
 * */
@Entity(name = "token")
@NoArgsConstructor
@Getter
@ToString
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private String memberEmail;
    private Date accessTokenTime;
    private Date refreshTokenTime;
    private String nickName;


    @Builder
    public TokenEntity(Long id,
                       String grantType,
                       String accessToken,
                       String refreshToken,
                       String memberEmail,
                       Date accessTokenTime,
                       Date refreshTokenTime,
                       String nickName) {
        this.id = id;
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberEmail = memberEmail;
        this.accessTokenTime = accessTokenTime;
        this.refreshTokenTime = refreshTokenTime;
        this.nickName = nickName;
    }

    // 토큰 엔티티로 변환
    public static TokenEntity tokenEntity(TokenDTO token) {
        return TokenEntity.builder()
                .grantType(token.getGrantType())
                .accessToken(token.getAccessToken())
                .accessTokenTime(token.getAccessTokenTime())
                .refreshToken(token.getRefreshToken())
                .refreshTokenTime(token.getRefreshTokenTime())
                .memberEmail(token.getMemberEmail())
                .nickName(token.getNickName())
                .build();
    }

    // 토큰 업데이트
    public void updateToken(TokenDTO token) {
        this.grantType = token.getGrantType();
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
    }
}
