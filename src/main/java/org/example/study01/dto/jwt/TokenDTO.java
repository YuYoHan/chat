package org.example.study01.dto.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.study01.entity.jwt.TokenEntity;

import java.util.Date;

@Getter
@ToString
@NoArgsConstructor
public class TokenDTO {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private String memberEmail;
    private Date accessTokenTime;
    private Date refreshTokenTime;
    private String nickName;

    @Builder
    public TokenDTO(
                    String grantType, 
                    String accessToken,
                    String refreshToken, 
                    String memberEmail, 
                    Date accessTokenTime, 
                    Date refreshTokenTime,
                    String nickName) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberEmail = memberEmail;
        this.accessTokenTime = accessTokenTime;
        this.refreshTokenTime = refreshTokenTime;
        this.nickName = nickName;
    }

    public static TokenDTO toTokenDTO(TokenEntity tokenEntity) {
        return TokenDTO.builder()
                .grantType(tokenEntity.getGrantType())
                .accessToken(tokenEntity.getAccessToken())
                .accessTokenTime(tokenEntity.getAccessTokenTime())
                .refreshToken(tokenEntity.getRefreshToken())
                .refreshTokenTime(tokenEntity.getAccessTokenTime())
                .memberEmail(tokenEntity.getMemberEmail())
                .nickName(tokenEntity.getNickName())
                .build();
    }
}
