
package com.smt.sabkamaal.holder;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@SuppressWarnings("unused")
public class LoginData implements Serializable {

    @SerializedName("email")
    private String mEmail;
    @SerializedName("full_name")
    private String mFullName;
    @SerializedName("mobile")
    private String mMobile;
    @SerializedName("user_id")
    private String mUserId;

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

    @Override
    public String toString() {
        return "LoginData{" +
                "mEmail='" + mEmail + '\'' +
                ", mFullName='" + mFullName + '\'' +
                ", mMobile='" + mMobile + '\'' +
                ", mUserId='" + mUserId + '\'' +
                '}';
    }

}
