package com.smt.sabkamaal.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.DefaultHttpClient;
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
        if (!dialog.isShowing()) {
            dialog.show();
        }
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
