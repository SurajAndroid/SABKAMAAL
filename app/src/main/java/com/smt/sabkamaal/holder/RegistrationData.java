
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
       @SerializedName("address")
    private String mAddress;
    @SerializedName("gumasta")
    private String mGumastaImage;
    @SerializedName("adhaar")
    private String mAdhaarImage;

    public String getmGumastaImage() {
        return mGumastaImage;
    }

    public void setmGumastaImage(String mGumastaImage) {
        this.mGumastaImage = mGumastaImage;
    }

    public String getmAdhaarImage() {
        return mAdhaarImage;
    }

    public void setmAdhaarImage(String mAdhaarImage) {
        this.mAdhaarImage = mAdhaarImage;
    }



    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }


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
