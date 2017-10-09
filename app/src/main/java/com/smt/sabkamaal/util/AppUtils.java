package com.smt.sabkamaal.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.inputmethod.InputMethodManager;

import com.smt.sabkamaal.dto.HistoryDTO;
import com.smt.sabkamaal.dto.ProductDTO;
import com.smt.sabkamaal.dto.ProfileDTO;

import java.util.ArrayList;

/**
 * Created by Nilesh Rathore on 13/3/17.
 */

public class AppUtils {

    private static AppUtils _instance;
    private static Context context;
    public static ArrayList<ProductDTO> productList = new ArrayList<>();

    public static ArrayList<ProfileDTO> profileList = new ArrayList<>();
    public static ArrayList<HistoryDTO> historyList = new ArrayList<>();


    private AppUtils() {
    }

    public static AppUtils getInstance(Context iContext){
        context = iContext;
        if (null == _instance){
            _instance = new AppUtils();
        }
        return _instance;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
        activity.getCurrentFocus().getWindowToken(), 0);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
