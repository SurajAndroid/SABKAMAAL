package com.smt.sabkamaal.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
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
import com.smt.sabkamaal.util.Constant;
import com.smt.sabkamaal.util.RequestReceiver;
import com.smt.sabkamaal.util.WebserviceHelper;
import com.smt.sabkamaal.webService.WebServiceUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements RequestReceiver{
    private static final String TAG = "SignupActivity";
    RequestReceiver receiver;
    EditText _address,_fullnameText, _emailText, _mobileText, _passwordText;
    TextView AddAdhar, AddGumashta, link_login;
    AppCompatButton btn_signup;

    String  imgStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);

        _address = (EditText)findViewById(R.id._address);
        _fullnameText = (EditText)findViewById(R.id._fullnameText);
        _emailText = (EditText)findViewById(R.id._emailText);
        _mobileText = (EditText)findViewById(R.id._mobileText);
        _passwordText = (EditText)findViewById(R.id._passwordText);
        btn_signup = (AppCompatButton)findViewById(R.id.btn_signup);

        link_login = (TextView)findViewById(R.id.link_login);
        AddAdhar = (TextView)findViewById(R.id.AddAdhar);
        AddGumashta = (TextView)findViewById(R.id.AddGumashta);

        receiver = this;

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtils.getInstance(SignUpActivity.this).isNetworkConnected()) {
                    signup();
                } else {
                    Toast.makeText(SignUpActivity.this, "Please connect to internet to continue!", Toast.LENGTH_LONG).show();
                }
            }
        });

        AddAdhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
                imgStatus = "1";
            }
        });

        AddGumashta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
                imgStatus = "2";
            }
        });

        link_login.setOnClickListener(new View.OnClickListener() {
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



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();

            if(imgStatus.equals("1")){
                Constant.ADHAR = getRealPathFromUri(SignUpActivity.this,selectedImage);
            }else {
                Constant.GUMASHTA = getRealPathFromUri(SignUpActivity.this,selectedImage);
            }
        }
    }


    public String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            btn_signup.setEnabled(true);
            return;
        }

        signUpService();
    }

    public void signUpService() {
        WebserviceHelper login = new WebserviceHelper(receiver, SignUpActivity.this);
        login.setAction(Constant.SIGNUP);
        login.execute();
    }

    public boolean validate() {
        boolean valid = true;

        String name = _fullnameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String address = _address.getText().toString();




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

        if (address.isEmpty() || address.matches("[A-Za-z0-9_]+")) {
            _address.setError("Enter address");
            valid = false;
        } else {
            _address.setError(null);
        }

        Constant.USER_NAME = _fullnameText.getText().toString();
        Constant.EMAIl= _emailText.getText().toString();
        Constant.MOBILE = _mobileText.getText().toString();
        Constant.PASSWORD = _passwordText.getText().toString();
        Constant.ADDRESS = _address.getText().toString();

        return valid;
    }

    @Override
    public void requestFinished(String[] result) throws Exception {
        if(result[0].equals("1")){

        }
    }

}