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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.smt.sabkamaal.R;
import com.smt.sabkamaal.constant.AppConstants;
import com.smt.sabkamaal.constant.URLConstant;

import com.smt.sabkamaal.holder.ForgotPassword;

import com.smt.sabkamaal.util.AppUtils;
import com.smt.sabkamaal.webService.WebServiceUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "ForgotPasswordActivity";

    @Bind(R.id.input_email)EditText _Emailtxt;
    @Bind(R.id.btn_submit)Button _Submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);

        _Submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (AppUtils.getInstance(ForgotPasswordActivity.this).isNetworkConnected()) {
            forget();
        } else {
            Toast.makeText(ForgotPasswordActivity.this, "Please connect to internet to continue!", Toast.LENGTH_LONG).show();
        }

    }
    public void forget() {
        Log.d(TAG, "Sucess");

        if (!validate()) {
            _Submit.setEnabled(true);
            return;
        }

        _Submit.setEnabled(false);
        String email = _Emailtxt.getText().toString();
        new ForgotPasswordTask(URLConstant.URL_FORGOTPASSWORD, email).execute();

    }





    public void onLoginSuccess(ForgotPassword forgetPassword) {
        _Submit.setEnabled(true);
        Toast.makeText(getApplicationContext(),forgetPassword.getMessage(),Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, ProductActivity.class));


    }

    public void onLoginFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        _Submit.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String email = _Emailtxt.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _Emailtxt.setError("enter a valid email address");
            valid = false;
        } else {
            _Emailtxt.setError(null);
        }



        return valid;
    }

    private class ForgotPasswordTask extends AsyncTask<String, String, String> {
        String email;
        String url;
        ProgressDialog progressDialog;

        ForgotPasswordTask(String url, String email) {
            this.email = email;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
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
                    .add(AppConstants.EMAIL, email)
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
                    ForgotPassword forgetPassword = new Gson().fromJson(json, ForgotPassword.class);
                    if (null != forgetPassword) {
                        if (forgetPassword.getSuccess().intValue() == 1) {
                            onLoginSuccess(forgetPassword);

                        } else if (null != forgetPassword.getMessage()) {
                            onLoginFailed(forgetPassword.getMessage());
                        } else {
                            onLoginFailed("Something went wrong!");
                        }
                    } else {
                        onLoginFailed("Something went wrong!");
                    }

                } else {
                    _Submit.setEnabled(true);
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                onLoginFailed("Something went wrong!");
                _Submit.setEnabled(true);
            }
        }
    }

}

