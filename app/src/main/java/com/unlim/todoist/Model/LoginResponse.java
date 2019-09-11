package com.unlim.todoist.Model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("token")
    private String token;

    private String errMessage;
    private int errCode;

    public String getErrMessage() {
        return errMessage;
    }

    public String getToken() {
        return token;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}
