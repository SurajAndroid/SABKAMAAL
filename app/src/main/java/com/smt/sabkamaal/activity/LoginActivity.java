package com.smt.sabkamaal.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.qintong.library.InsLoadingView;
import com.smt.sabkamaal.R;
import com.smt.sabkamaal.constant.AppConstants;
import com.smt.sabkamaal.constant.URLConstant;
import com.smt.sabkamaal.holder.Login;
import com.smt.sabkamaal.preference.PreferenceHelper;
import com.smt.sabkamaal.util.AppUtils;
import com.smt.sabkamaal.util.Constant;
import com.smt.sabkamaal.util.MarshMallowPermission;
import com.smt.sabkamaal.util.RequestReceiver;
import com.smt.sabkamaal.util.WebserviceHelper;
import com.smt.sabkamaal.webService.WebServiceUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.smt.sabkamaal.R.id.login;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RequestReceiver {
    RequestReceiver receiver;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    final Context context = this;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;
    @Bind(R.id.link_forgotpwd)
    TextView _forgotpwdLink;
    @Bind(R.id.rememberMe)CheckBox _rememberMe;

    @Bind(R.id.loader)
    InsLoadingView loading_view;

    SharedPreferences sharedPreferences;
    MarshMallowPermission marshMallowPermission;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ButterKnife.bind(this);

        receiver = this;
        loading_view.setVisibility(View.INVISIBLE);
        _rememberMe.setOnClickListener(this);
        _loginButton.setOnClickListener(this);
        _signupLink.setOnClickListener(this);
        _forgotpwdLink.setOnClickListener(this);

        try{
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        sharedPreferences = this.getSharedPreferences("login_status", Context.MODE_PRIVATE);

        boolean remember_Me = PreferenceHelper.getInstance(getApplicationContext()).getIsUserRememberMe();

        if(remember_Me) {
            //get previously stored login details
            String email = PreferenceHelper.getInstance(context).getUserEmailId();
            String password = PreferenceHelper.getInstance(context).getUserPassword();
            _emailText.setText(email);
            _passwordText.setText(password);
            _rememberMe.setChecked(true);

        }

        marshMallowPermission = new MarshMallowPermission(LoginActivity.this);
        if (Build.VERSION.SDK_INT >= 23) {
            if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                marshMallowPermission.requestPermissionForExternalStorage();

            }
        }

    }
    private void saveLoginDetails(){
        //fill input boxes with stored login and pass
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        PreferenceHelper.getInstance(context).setIsUserRememberMe(true);
        PreferenceHelper.getInstance(context).setUserEmailId(email);
        PreferenceHelper.getInstance(context).setUserPassword(password);

    }

    private void removeLoginDetails(){
        PreferenceHelper.getInstance(context).setIsUserRememberMe(false);
        PreferenceHelper.getInstance(context).removeUserEmailId();
        PreferenceHelper.getInstance(context).removeUserPassword();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:

                if (AppUtils.getInstance(LoginActivity.this).isNetworkConnected()) {
                    boolean isChecked = _rememberMe.isChecked();
                    if(isChecked)
                    {
                        saveLoginDetails();
                    }
                    else
                    {
                        removeLoginDetails();
                    }
                    login();

                } else {
                    Toast.makeText(LoginActivity.this, "Please connect to internet to continue!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.link_signup:
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case R.id.link_forgotpwd:
                Intent intent1 = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }


    public void loginService() {
        WebserviceHelper login = new WebserviceHelper(receiver, LoginActivity.this);
        login.setAction(Constant.LOGIN);
        login.execute();
        loading_view.setVisibility(View.VISIBLE);
    }

    public void login() {
        Log.d(TAG, "Login");
        if (!validate()) {
            _loginButton.setEnabled(true);
            return;
        }
//        _loginButton.setEnabled(false);
        Constant.MOBILE = _emailText.getText().toString();
        Constant.PASSWORD = _passwordText.getText().toString();

        loginService();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Your account created successfully!\nLogin to continue...", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

   /* public void onLoginSuccess(Login login) {
        _loginButton.setEnabled(true);
    }*/

    /*public void onLoginFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }*/

    public boolean validate() {
        boolean valid = true;
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    @Override
    public void requestFinished(String[] result) throws Exception {

        if(result[0].equals("1")){
            loading_view.setVisibility(View.INVISIBLE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_id", ""+AppUtils.profileList.get(0).getUser_id());
            editor.putString("full_name",""+AppUtils.profileList.get(0).getFull_name());
            editor.putString("email", ""+AppUtils.profileList.get(0).getEmail());
            editor.putString("mobile", ""+AppUtils.profileList.get(0).getMobile());
            editor.putString("address",""+AppUtils.profileList.get(0).getAddress());
            editor.putString("gumasta", ""+AppUtils.profileList.get(0).getGumasta());
            editor.putString("adhaar", ""+AppUtils.profileList.get(0).getAdhaar());
            editor.putString("status", ""+1);
            editor.commit();
            Intent intent = new Intent(this, ProductActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else {
            loading_view.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),""+result[1],Toast.LENGTH_SHORT).show();
        }
    }

    /*private class LoginTask extends AsyncTask<String, String, String> {
        String mobile;
        String password;
        String url;
        ProgressDialog progressDialog;

        LoginTask(String url, String email, String password) {
            this.mobile = email;
            this.password = password;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String[] params) {
            RequestBody requestBody = getRequestBody();
            try {
                String json = WebServiceUtil.getInstance(getApplicationContext()).getJsonFromPostMethod(url, requestBody);
                if (json != null) {
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @NonNull
        private RequestBody getRequestBody() {
            return new FormEncodingBuilder()
                    .add(AppConstants.EMAIL, mobile)
                    .add(AppConstants.PASSWORD, password)
                    .build();
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            try {

                if (null != progressDialog && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (null != json) {
                    try {
                        JSONObject object = new JSONObject(json);
                        Log.e("","Json : "+json.toString());
                        JSONObject dataObj = object.getJSONObject("data");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user_id", ""+dataObj.getString("user_id"));
                        editor.putString("full_name", ""+dataObj.getString("full_name"));
                        editor.putString("email", ""+dataObj.getString("email"));
                        editor.putString("mobile", ""+dataObj.getString("mobile"));
                        editor.putString("status", ""+1);
                        editor.commit();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Login login = new Gson().fromJson(json, Login.class);
                    if (null != login) {
                        if (login.getSuccess().intValue() == 1) {
                            onLoginSuccess(login);
                        } else if (null != login.getMessage()) {
                            onLoginFailed(login.getMessage());
                        } else {
                            onLoginFailed("Login Failed");
                        }
                    } else {
                        onLoginFailed("Something went wrong!");
                    }

                } else {
                    _loginButton.setEnabled(true);
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                onLoginFailed("Something went wrong!");
                _loginButton.setEnabled(true);
            }
        }
    }*/

}
