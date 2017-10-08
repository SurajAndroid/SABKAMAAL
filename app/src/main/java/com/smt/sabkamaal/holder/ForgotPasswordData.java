
package com.smt.sabkamaal.holder;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class ForgotPasswordData {

    @SerializedName("new_password")
    private String mNewPassword;

    public String getNewPassword() {
        return mNewPassword;
    }

    public void setNewPassword(String newPassword) {
        mNewPassword = newPassword;
    }

}
