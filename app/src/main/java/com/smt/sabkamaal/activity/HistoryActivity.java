package com.smt.sabkamaal.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.smt.sabkamaal.R;
import com.smt.sabkamaal.adapter.AllProductAdapter;
import com.smt.sabkamaal.adapter.ImageSliderAdapter;
import com.smt.sabkamaal.adapter.OrderHistoryAdapter;
import com.smt.sabkamaal.adapter.ProductGridAdapter;
import com.smt.sabkamaal.constant.AppConstants;
import com.smt.sabkamaal.constant.URLConstant;
import com.smt.sabkamaal.dto.ProductDTO;
import com.smt.sabkamaal.holder.CategoryWiseProduct;
import com.smt.sabkamaal.holder.ProductCategory;
import com.smt.sabkamaal.holder.ProductCategoryData;
import com.smt.sabkamaal.util.AppUtils;
import com.smt.sabkamaal.util.Constant;
import com.smt.sabkamaal.util.Global;
import com.smt.sabkamaal.util.RequestReceiver;
import com.smt.sabkamaal.util.WebserviceHelper;
import com.smt.sabkamaal.webService.WebServiceUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import me.relex.circleindicator.CircleIndicator;

public class HistoryActivity extends AppCompatActivity implements RequestReceiver{

    RequestReceiver receiver;
    DrawerLayout drawer_layout;
    LinearLayout slidMenuLayout;
    TextView rateUsTxt, offerTxt, rewardTxt, termsTxt, logoutTxt, orderHistoryTxt, homeTxt;
    SharedPreferences sharedPreferences;
    ListView history_list;
    OrderHistoryAdapter orderHistoryAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_main_new);
        init();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (AppUtils.getInstance(HistoryActivity.this).isNetworkConnected()) {
            loginService();
        } else {
            Toast.makeText(HistoryActivity.this, "Please connect to internet to continue!", Toast.LENGTH_LONG).show();
        }
    }

    public void loginService() {
        WebserviceHelper history = new WebserviceHelper(receiver, HistoryActivity.this);
        history.setAction(Constant.HISTORY);
        history.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HistoryActivity.this, ProductActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void init() {
        receiver = this;
        sharedPreferences = this.getSharedPreferences("login_status", Context.MODE_PRIVATE);
        history_list = (ListView)findViewById(R.id.history_list);
        Constant.USER_ID = sharedPreferences.getString("user_id","");
        rateUsTxt = (TextView)findViewById(R.id.rateUsTxt);
        offerTxt = (TextView)findViewById(R.id.offerTxt);
        rewardTxt = (TextView)findViewById(R.id.rewardTxt);
        termsTxt = (TextView)findViewById(R.id.termsTxt);
        logoutTxt = (TextView)findViewById(R.id.logoutTxt);
        orderHistoryTxt = (TextView)findViewById(R.id.orderHistoryTxt);
        homeTxt = (TextView)findViewById(R.id.homeTxt);

        slidMenuLayout = (LinearLayout)findViewById(R.id.slidMenuLayout);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);


        /*Click Listener*/
        slidMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(GravityCompat.START);
            }
        });

        homeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawers();
                Intent intent = new Intent(HistoryActivity.this, ProductActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        rateUsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawers();
            }
        });

        offerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawers();
            }
        });

        rewardTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawers();
            }
        });

        termsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawers();
            }
        });

        logoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawers();
                sharedPreferences =  HistoryActivity.this.getSharedPreferences("login_status",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(HistoryActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        orderHistoryTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void requestFinished(String[] result) throws Exception {
        if(result[0].equals("1")){
            orderHistoryAdapter = new OrderHistoryAdapter(HistoryActivity.this, AppUtils.historyList);
            history_list.setAdapter(orderHistoryAdapter);
        }else {
            Toast.makeText(getApplicationContext(),""+result[1],Toast.LENGTH_SHORT).show();
        }
    }
}
