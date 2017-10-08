
package com.smt.sabkamaal.holder;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class ForgotPassword {

    @SerializedName("data")
    private ForgotPasswordData mData;
    @SerializedName("error")
    private Long mError;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("rccode")
    private String mRccode;
    @SerializedName("success")
    private Long mSuccess;

    public ForgotPasswordData getData() {
        return mData;
    }

    public void setData(ForgotPasswordData data) {
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

}
