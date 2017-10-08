
package com.smt.sabkamaal.holder;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class RegistrationData {

    @SerializedName("email")
    private String mEmail;
    @SerializedName("full_name")
    private String mFullName;
    @SerializedName("mobile")
    private String mMobile;
    @SerializedName("user_id")
    private String mUserId;
    @SerializedName("password")
    private String mPassword;

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String Password) {
        mPassword = Password;
    }



    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        mFullName = fullName;
    }

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

}
