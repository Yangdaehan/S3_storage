package org.mse.s3_storage.com.dto;


import lombok.Getter;

@Getter
public class MemberRequest {

    public MemberRequest() {}

    private String memberId;

    public String getMemberId() {
        return memberId;
    }
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
