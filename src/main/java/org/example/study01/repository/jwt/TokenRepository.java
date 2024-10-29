package org.example.study01.repository.jwt;

import org.example.study01.entity.jwt.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    TokenEntity findByMemberEmail(String memberEmail);
}
