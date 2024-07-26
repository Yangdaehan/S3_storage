package org.mse.s3_storage.com.dto;

import lombok.Getter;

@Getter
public class SubfolderRequest {
    public SubfolderRequest() {}

    private String subfolderName;

    public String getSubfolderName() {
        return subfolderName;
    }
    public void setSubfolderName(String subfolderName) {
        this.subfolderName = subfolderName;
    }
}
