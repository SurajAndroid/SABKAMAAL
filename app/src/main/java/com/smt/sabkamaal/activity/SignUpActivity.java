package com.smt.sabkamaal.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.smt.sabkamaal.util.imageutils.ImageLoader;
import com.smt.sabkamaal.webService.WebServiceUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    @Bind(R.id.input_name)EditText _fullnameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_mobile) EditText _mobileText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_address) EditText _addresstext;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;
    @Bind(R.id.adhar_photo) ImageView _adharimage;
    @Bind(R.id.gumasta_photo) ImageView _gumastaimage;
    private String gumastapicturePath,adharpicturepath;

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
        _adharimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             selectAdharImage();
            }
        });
        _gumastaimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGumastaImage();
            }
        });

    }
    private void selectGumastaImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(SignUpActivity.this);

                if (options[item].equals("Take Photo")) {
                    if (result) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                       startActivityForResult(intent, 3);
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    if (result) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 4);
                    }

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

//    @Override
//    protected void onActivityResult(int requestCode1, int resultCode1, Intent data1) {
//        super.onActivityResult(requestCode1, resultCode1, data1);
//        if (resultCode1 == RESULT_OK) {
//            if (requestCode1 == 1) {
//                File f = new File(Environment.getExternalStorageDirectory().toString());
//                for (File temp : f.listFiles()) {
//                    if (temp.getName().equals("temp.jpg")) {
//                        f = temp;
//                        gumastapicturePath = f.getAbsolutePath();
//                        Log.d("FilePath:","Camera: "+f.getAbsolutePath());
//                        break;
//                    }
//                }
//                try {
//                    Bitmap bitmap;
//                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//
//                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
//                            bitmapOptions);
//
//                    _gumastaimage.setImageBitmap(bitmap);
//
//                    String path = Environment
//                            .getExternalStorageDirectory()
//                            + File.separator
//                            + "Phoenix" + File.separator + "default";
////                    f.delete();
//                    OutputStream outFile = null;
//                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
//                    try {
//                        outFile = new FileOutputStream(file);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
//                        outFile.flush();
//                        outFile.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else if (requestCode1 == 2) {
//                Uri selectedImage = data1.getData();
//                String[] filePath = {MediaStore.Images.Media.DATA};
//                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePath[0]);
//                adharpicturepath = c.getString(columnIndex);
//                Log.d("FilePath:","Gallery: "+adharpicturepath);
//                c.close();
//                Bitmap thumbnail = (BitmapFactory.decodeFile(adharpicturepath));
//                _adharimage.setImageBitmap(thumbnail);
//            }
//
//        }
//    }
    private void selectAdharImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(SignUpActivity.this);

                if (options[item].equals("Take Photo")) {
                    if (result) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, 1);
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    if (result) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        adharpicturepath = f.getAbsolutePath();
                        Log.d("FilePath:","Camera: "+f.getAbsolutePath());
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    _adharimage.setImageBitmap(bitmap);

                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
//                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                adharpicturepath = c.getString(columnIndex);
                Log.d("FilePath:","Gallery: "+adharpicturepath);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(adharpicturepath));
                _adharimage.setImageBitmap(thumbnail);
            }

        }
        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        gumastapicturePath = f.getAbsolutePath();
                        Log.d("FilePath:","Camera: "+f.getAbsolutePath());
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    _gumastaimage.setImageBitmap(bitmap);

                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
//                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 4) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                gumastapicturePath = c.getString(columnIndex);
                Log.d("FilePath:","Gallery: "+gumastapicturePath);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(gumastapicturePath));
                _gumastaimage.setImageBitmap(thumbnail);
            }

        }
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
        data.setAddress(_addresstext.getText().toString());
        data.setmAdhaarImage(adharpicturepath);
        data.setmGumastaImage(gumastapicturePath);

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
        String address = _addresstext.getText().toString();


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
        if (address.isEmpty()) {
            _addresstext.setError("Enter your address");
            valid = false;
        } else {
            _addresstext.setError(null);
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
            File sourceFile = null;
            File sourceFile1 = null;
            RequestBody fileBody = null;
            RequestBody fileBody1 = null;
            String contentType, contentType1;
            RequestBody requestBody;
            try {
                String adhaarImage = data.getmAdhaarImage();
                String gumastaImage = data.getmGumastaImage();
                if (null != adhaarImage && null != gumastaImage) {
                    sourceFile = new File(adhaarImage);
                    sourceFile1 = new File(gumastaImage);
                }
                if (null != adhaarImage && sourceFile.exists() || null != gumastaImage && sourceFile1.exists()) {
                    contentType = sourceFile.toURL().openConnection().getContentType();
                    fileBody = RequestBody.create(MediaType.parse(contentType), sourceFile);
                    contentType1 = sourceFile1.toURL().openConnection().getContentType();
                    fileBody1 = RequestBody.create(MediaType.parse(contentType1), sourceFile1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            requestBody =  new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart(AppConstants.FULL_NAME, data.getFullName())
                    .addFormDataPart(AppConstants.MOBILE, data.getMobile())
                    .addFormDataPart(AppConstants.EMAIL, data.getEmail())
                    .addFormDataPart(AppConstants.PASSWORD, data.getPassword())
                    .addFormDataPart(AppConstants.ADDRESS,data.getAddress())
                    .addFormDataPart(AppConstants.ADHAAR,sourceFile.getName(),fileBody)
                    .addFormDataPart(AppConstants.GUMASTA,sourceFile1.getName(),fileBody1)
                    .build();


        return requestBody;
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
    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context) {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }
}