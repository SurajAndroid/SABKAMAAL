
package com.smt.sabkamaal.holder;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@SuppressWarnings("unused")
public class Login implements Serializable {

    @SerializedName("data")
    private LoginData mData;
    @SerializedName("error")
    private Long mError;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("rccode")
    private String mRccode;
    @SerializedName("success")
    private Long mSuccess;

    public LoginData getData() {
        return mData;
    }

    public void setData(LoginData data) {
        mData = data;
    }

    public Long getError() {
        return mError;
    }

    public void setError(Long error) {
        mError = error;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getRccode() {
        return mRccode;
    }

    public void setRccode(String rccode) {
        mRccode = rccode;
    }

    public Long getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Long success) {
        mSuccess = success;
    }


    @Override
    public String toString() {
        return "Login{" +
                "mData=" + mData +
                ", mError=" + mError +
                ", mMessage='" + mMessage + '\'' +
                ", mRccode='" + mRccode + '\'' +
                ", mSuccess=" + mSuccess +
                '}';
    }


}
