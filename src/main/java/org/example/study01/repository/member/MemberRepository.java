package org.example.study01.repository.member;

import org.example.study01.entity.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    MemberEntity findByEmail(String email);
    void deleteByMemberId(Long memberId);
    MemberEntity findByNickName(String nickName);
}
