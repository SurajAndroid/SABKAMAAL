package com.smt.sabkamaal.preference;

import android.content.Context;
import android.content.SharedPreferences;



public class PreferenceHelper implements PreferenceConstants {

    private static PreferenceHelper _instance;
    private static Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private PreferenceHelper() {
        sharedPreferences = context.getSharedPreferences(SHARED_PRE_LOGIN_REGISTRATION, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static PreferenceHelper getInstance(Context iContext){
        context = iContext;
        if (_instance == null){
            _instance = new PreferenceHelper();
        }
        return _instance;
    }

    public boolean getIsUserLoggedIn(){
        return sharedPreferences.getBoolean(IS_USER_LOGGED_IN, false);
    }

    public void setIsUserLoggedIn(boolean isUserLoggedIn){
        editor.putBoolean(IS_USER_LOGGED_IN, isUserLoggedIn);
        editor.commit();
    }

    public boolean getIsUserRememberMe(){
        return sharedPreferences.getBoolean(REMEMBER_ME, false);
    }

    public void setIsUserRememberMe(boolean rememberMe){
        editor.putBoolean(REMEMBER_ME, rememberMe);
        editor.commit();
    }

    public String getUserPassword(){
        return sharedPreferences.getString(USER_PASSWORD, null);
    }

    public void setUserPassword(String password){
        editor.putString(USER_PASSWORD, password);
        editor.commit();
    }
    public void removeUserPassword(){
        editor.remove(USER_PASSWORD);
        editor.commit();
    }

    public String getUserEmailId(){
        return sharedPreferences.getString(USER_MAIL_ID,null );
    }

    public void setUserEmailId(String emailId){
        editor.putString(USER_MAIL_ID, emailId);
        editor.commit();
    }
    public void removeUserEmailId(){
        editor.remove(USER_MAIL_ID);
        editor.commit();
    }

}

