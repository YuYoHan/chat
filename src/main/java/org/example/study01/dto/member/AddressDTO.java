package org.example.study01.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
 *   writer : 유요한
 *   work :
 *          주소에 대한 ResponseDTO
 *   date : 2023/11/01
 * */
@ToString
@Getter
@NoArgsConstructor
public class AddressDTO {
    private String memberAddr;
    private String memberAddrDetail;
    private String memberZipCode;

    @Builder
    public AddressDTO(String memberAddr, String memberAddrDetail, String memberZipCode) {
        this.memberAddr = memberAddr;
        this.memberAddrDetail = memberAddrDetail;
        this.memberZipCode = memberZipCode;
    }

}
