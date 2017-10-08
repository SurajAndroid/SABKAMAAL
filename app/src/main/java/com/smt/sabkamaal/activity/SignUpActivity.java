package com.smt.sabkamaal.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smt.sabkamaal.R;
import com.smt.sabkamaal.constant.AppConstants;
import com.smt.sabkamaal.constant.URLConstant;
import com.smt.sabkamaal.holder.LoginData;
import com.smt.sabkamaal.holder.Registration;
import com.smt.sabkamaal.holder.RegistrationData;
import com.smt.sabkamaal.util.AppUtils;
import com.smt.sabkamaal.webService.WebServiceUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_name)EditText _fullnameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_mobile) EditText _mobileText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtils.getInstance(SignUpActivity.this).isNetworkConnected()) {
                    signup();
                } else {
                    Toast.makeText(SignUpActivity.this, "Please connect to internet to continue!", Toast.LENGTH_LONG).show();
                }
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
//            onSignupFailed("SignUp Failed");
            _signupButton.setEnabled(true);
            return;
        }

        _signupButton.setEnabled(false);

        RegistrationData data = new RegistrationData();
        data.setFullName(_fullnameText.getText().toString());
        data.setEmail(_emailText.getText().toString());
        data.setMobile(_mobileText.getText().toString());
        data.setPassword(_passwordText.getText().toString());

        new SignUpTask(URLConstant.URL_REGISTRATION, data).execute();

    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _fullnameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();


        if (name.isEmpty() || name.length() < 3 || name.matches("[A-Za-z0-9_]+")) {
            _fullnameText.setError("Enter the full name");
            valid = false;
        } else {
            _fullnameText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }


        return valid;
    }


    private class SignUpTask extends AsyncTask<String, String, String> {

        String url;
        RegistrationData data;
        ProgressDialog progressDialog;

        SignUpTask(String url, RegistrationData data) {
            this.data = data;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SignUpActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String[] params) {
            RequestBody requestBody = getRequestBody();
            String json = WebServiceUtil.getInstance(getApplicationContext()).getJsonFromPostMethod(url, requestBody);
            if (json != null) return json;

            return null;
        }

        @NonNull
        private RequestBody getRequestBody() {
            return new FormEncodingBuilder()
                    .add(AppConstants.FULL_NAME, data.getFullName())
                    .add(AppConstants.MOBILE, data.getMobile())
                    .add(AppConstants.EMAIL, data.getEmail())
                    .add(AppConstants.PASSWORD, data.getPassword())
                    .build();
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            if (null != progressDialog && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (null != json) {
                Registration registration = new Gson().fromJson(json, Registration.class);
                if (null != registration) {
                    if (registration.getSuccess().intValue() == 1) {
                        onSignupSuccess();
                    } else if (null != registration.getMessage()) {
                        onSignupFailed(registration.getMessage());
                    } else {
                        onSignupFailed("SignUp Failed");
                    }
                } else {
                    onSignupFailed("SignUp Failed");
                }

            }

        }
    }
}