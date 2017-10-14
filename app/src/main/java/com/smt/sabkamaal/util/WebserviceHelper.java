package com.smt.sabkamaal.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.smt.sabkamaal.dto.HistoryDTO;
import com.smt.sabkamaal.dto.ProfileDTO;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




//import org.apache.http.entity.mime.MultipartEntity;
@SuppressWarnings("deprecation")
public class WebserviceHelper extends AsyncTask<Void, Void, String[]> {

    private RequestReceiver mContext;
    @SuppressWarnings("unused")
    private String method = null;
    private Map<String, String> paramMap = new HashMap<String, String>();
    private String errorMessage;
    private boolean error_flag = false;
    ProgressDialog mProgressDialog;

    public static int action;

    ProgressDialog dialog;
    Activity mcont;
    SharedPreferences sharedPreferences;

    public WebserviceHelper() {
    }

    public WebserviceHelper(RequestReceiver context, Activity mcontext) {
        mContext = context;
        mcont = mcontext;
        dialog = new ProgressDialog(mcontext);
        sharedPreferences = mcontext.getSharedPreferences("login_status", Context.MODE_PRIVATE);
    }

    WebserviceHelper(RequestReceiver context, String setMethod) {
        mContext = context;
        method = setMethod;
    }

    private void clearErrors() {
        this.errorMessage = null;
        this.error_flag = false;
    }

    public void setMethod(String m) {
        method = m;
    }

    public void addParam(String key, String value) {
        paramMap.put(key, value);
    }

    @Override
    protected void onPreExecute() {
        this.clearErrors();

        dialog.setMessage("Please Wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        /*if (!dialog.isShowing()) {
            dialog.show();
        }*/
    }

    @SuppressWarnings("unchecked")
    @Override
    protected String[] doInBackground(Void... params) {

        Log.e("in background", "");
        Log.d("d  in background", "");
        // Create a newhome HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        JSONObject jsonObj = new JSONObject();
        HttpResponse response1 = null;
        HttpPost httppost = null;
        HttpGet httpGet = null;
        JSONObject jsonData = new JSONObject();
        switch (action) {

            case Constant.CREATEORDER:
                String[] order = new String[3];
                httppost = new HttpPost(Constant.CREATE_ORDER_URL);
                try {
                    try {
                        JSONArray array = new JSONArray();
                        JSONObject object = null;
                        for(int i =0 ;i<AppUtils.productList.size();i++){
                            object = new JSONObject();
                            if(!AppUtils.productList.get(i).getCustomer_quantity().equals("")){
                                object.put("product_id",AppUtils.productList.get(i).getCat_id());
                                object.put("qty",AppUtils.productList.get(i).getCustomer_quantity());
                                object.put("price",AppUtils.productList.get(i).getPrice());
                                object.put("total",""+( Double.parseDouble((AppUtils.productList.get(i).getPrice())) *  Double.parseDouble((AppUtils.productList.get(i).getCustomer_quantity()))) );
                                object.put("product_name",AppUtils.productList.get(i).getProduct_name());
                                array.put(object);
                            }
                        }

                        jsonData.accumulate("user_id",sharedPreferences.getString("user_id",""));
                        jsonData.accumulate("username",sharedPreferences.getString("full_name",""));
                        jsonData.accumulate("order",array);

                        Log.e("", "URL " + Constant.CREATE_ORDER_URL);
                        Log.e("Json : ", "" + jsonData.toString(5));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);
                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            order[0] = object.getString("success");
                            order[1] = object.getString("message");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            order[0] = object.getString("success");
                            order[1] = object.getString("message");
                        }
                        break;
                    }
                    return order;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.LOGIN:
                String[] login = new String[3];
                httppost = new HttpPost(Constant.LOGIN_URL);

                    try {
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                        nameValuePairs.add(new BasicNameValuePair("mobile", Constant.MOBILE));
                        nameValuePairs.add(new BasicNameValuePair("password", Constant.PASSWORD));

                        Log.e("", "nameValuePairs   " + nameValuePairs.toString());
                        try {
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                            Log.d("myapp", "works till here. 2");
                            try {
                                response1 = httpclient.execute(httppost);
                                response1.getStatusLine().getStatusCode();
                                Log.e("myapp", "response.. statau.." + response1.getStatusLine().getStatusCode());
                                Log.e("myapp", "response.. " + response1.getEntity());
                            } catch (ClientProtocolException e) {
                                e.printStackTrace();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        InputStream inputStream = response1.getEntity().getContent();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                        StringBuilder sb = new StringBuilder();
                        sb.append(reader.readLine() + "\n");
                        String line = "0";
                        String result = "";
                        JSONObject object = null;
                        AppUtils.profileList.clear();
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                            result = sb.toString();
                            Log.e("", "encodeRes : " + result);
                            try {
                                object = new JSONObject(result);
                                Log.d("", "jsonObj responce... " + object);
                                login[0] = object.getString("success");
                                login[1] = object.getString("message");
                                JSONObject object1 = object.getJSONObject("data");
                                ProfileDTO profileDTO = new ProfileDTO();

                                profileDTO.setUser_id(object1.getString("user_id"));
                                profileDTO.setFull_name(object1.getString("full_name"));
                                profileDTO.setEmail(object1.getString("email"));
                                profileDTO.setMobile(object1.getString("mobile"));
                                profileDTO.setAddress(object1.getString("address"));
                                profileDTO.setGumasta(object1.getString("gumasta"));
                                profileDTO.setAdhaar(object1.getString("adhaar"));

                                AppUtils.profileList.add(profileDTO);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                login[0] = object.getString("success");
                                login[1] = object.getString("message");
                            }
                            break;
                        }
                        return login;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;


            case Constant.SIGNUP:
                String[] regParm = new String[3];
                httppost = new HttpPost(Constant.SIGN_UP_URL);
                Log.e("", "Constant.SIGNUP_URL : " + Constant.SIGN_UP_URL);
                try {
                    MultipartEntity entity = new MultipartEntity();

                    try {
                        Log.e("", "ImagePathe : " + Constant.GUMASHTA);
                        File file = new File(Constant.GUMASHTA);
                        FileBody bin = new FileBody(file);
                        entity.addPart("gumasta_no", bin);
                    } catch (Exception e) {
                        Log.v("Exception in Image", "" + e);
                    }

                    try {
                        Log.e("", "Image Adhar : " + Constant.ADHAR);
                        File file = new File(Constant.ADHAR);
                        FileBody bin = new FileBody(file);
                        entity.addPart("adhaar_no", bin);
                    } catch (Exception e) {
                        Log.v("Exception in Image", "" + e);
                    }

                    entity.addPart("full_name", new StringBody(Constant.USER_NAME));
                    entity.addPart("email", new StringBody(Constant.EMAIl));
                    entity.addPart("password", new StringBody(Constant.PASSWORD));
                    entity.addPart("mobile", new StringBody(Constant.MOBILE));
                    entity.addPart("address", new StringBody(Constant.ADDRESS));

                    httppost.setEntity(entity);
                    try {
                        response1 = httpclient.execute(httppost);
                        Log.d("myapp", "response " + response1.getEntity());
                        Log.e("myapp", "response.. statau.." + response1.getStatusLine().getStatusCode());
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    InputStreamReader inputStreamReader = new InputStreamReader(
                            inputStream);
                    BufferedReader bufferedReader = new BufferedReader(
                            inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String bufferedStrChunk = null;
                    String encodeRes = "";

                    JSONObject object = null;

                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                        stringBuilder.append(bufferedStrChunk);
                        encodeRes = stringBuilder.toString();
                        try {
                            object = new JSONObject(encodeRes);
                            Log.d("", "jsonObj responce... " + object);
                            regParm[0] = object.getString("success");
                            regParm[1] = object.getString("message");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            regParm[0] = object.getString("success");
                            regParm[1] = object.getString("message");
                        }
                        break;
                    }

                    return regParm;

                } catch (Exception e) {
                    // TODO: handle exception
                }

                break;

            case Constant.HISTORY:
                String[] history = new String[3];
                httppost = new HttpPost(Constant.HISTORY_URL);

                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("customer_id", Constant.USER_ID));


                    Log.e("", "nameValuePairs   " + nameValuePairs.toString());
                    try {
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        Log.d("myapp", "works till here. 2");
                        try {
                            response1 = httpclient.execute(httppost);
                            response1.getStatusLine().getStatusCode();
                            Log.e("myapp", "response.. statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    AppUtils.historyList.clear();
                    HistoryDTO historyDTO = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);
                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            history[0] = object.getString("success");
                            history[1] = object.getString("message");
//                            JSONObject object1 = object.getJSONObject("data");
                            JSONArray array = object.getJSONArray("data");

                            for(int i=0;i<array.length();i++){

                                JSONObject arrObj = array.getJSONObject(i);
                                JSONArray inerArry = arrObj.getJSONArray("orders");

                                for(int j=0;j<inerArry.length();j++){

                                    JSONObject object2 = inerArry.getJSONObject(j);
                                    historyDTO = new HistoryDTO();
                                    historyDTO.setOrder_id(arrObj.getString("order_id"));
                                    historyDTO.setDate_time(arrObj.getString("created_at"));
                                    historyDTO.setProduct_id(object2.getString("product_id"));
                                    historyDTO.setQty(object2.getString("qty"));
                                    historyDTO.setPrice(object2.getString("price"));
                                    historyDTO.setTotal(object2.getString("total"));
                                    historyDTO.setProduct_name(object2.getString("product_name"));
                                    AppUtils.historyList.add(historyDTO);

                                }

                            }

                            Log.e("","Size : "+AppUtils.historyList.size());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            history[0] = object.getString("success");
                            history[1] = object.getString("message");
                        }
                        break;
                    }
                    return history;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


                    default:
                break;
        }
        return null;
    }


    @Override
    protected void onPostExecute(String[] result) {

        if (dialog.isShowing()) {
            dialog.cancel();
        }
        try {
            ((RequestReceiver) mContext).requestFinished(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;

    }

    public boolean errors_occurred() {
        return this.error_flag;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    private void releaseListMemory() {

    }

    public void setAction(int action) {
        WebserviceHelper.action = action;
    }

}
