package com.smt.sabkamaal.webService;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by N!LeSh on 3/8/2017.
 */

public class WebServiceUtil {

    static WebServiceUtil _instance;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String TAG = WebServiceUtil.class.getSimpleName();


    private WebServiceUtil() {
    }

    public static WebServiceUtil getInstance(Context applicationContext) {
        if (null == _instance) {
            _instance = new WebServiceUtil();
        }
        return _instance;
    }
    @Nullable
    public String getJsonFromPostMethod(String url, RequestBody requestBody) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
            okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            Call call = okHttpClient.newCall(request);

            Response response = call.execute();
            Log.d(TAG, "Response response:" + response);


            if (null != response) {
                Log.d(TAG, "getJsonFromPostMethod() Response code:" + response.code());
                String json = new JSONObject(response.body().string()).toString();
                Log.d(TAG, "Response code:" + response.code());
                Log.d(TAG, "Response JSON:" + json);
                return json;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public String getJsonFromGetMethod(String url) {
        Log.d(TAG, "getJsonFromGetMethod() URL: "+url);
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
            okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Call call = okHttpClient.newCall(request);

            Response response = call.execute();
            Log.d(TAG, "getJsonFromGetMethod() Response: " + response);

            if (null != response && response.code() == 200) {
                Log.d(TAG, "getJsonFromGetMethod() Response code:" + response.code());
                String json = new JSONObject(response.body().string()).toString();
                Log.d(TAG, "getJsonFromGetMethod() Response JSON:" + json);
                return json;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "getJsonFromGetMethod() Exception: "+e.getMessage());
        }
        return null;
    }
}
