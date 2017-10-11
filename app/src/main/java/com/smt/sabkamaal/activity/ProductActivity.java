package com.smt.sabkamaal.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.smt.sabkamaal.R;
import com.smt.sabkamaal.adapter.AllProductAdapter;
import com.smt.sabkamaal.adapter.ImageSliderAdapter;
import com.smt.sabkamaal.adapter.ProductGridAdapter;
import com.smt.sabkamaal.constant.AppConstants;
import com.smt.sabkamaal.constant.Productlist;
import com.smt.sabkamaal.constant.URLConstant;
import com.smt.sabkamaal.dto.ProductDTO;
import com.smt.sabkamaal.holder.CategoryWiseProduct;
import com.smt.sabkamaal.holder.ProductCategory;
import com.smt.sabkamaal.holder.ProductCategoryData;
import com.smt.sabkamaal.util.AppUtils;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import me.relex.circleindicator.CircleIndicator;

public class ProductActivity extends AppCompatActivity {

    private AllProductAdapter mAdapter;
    private ProductGridAdapter productGridAdapter;
    GridView  product_grid;
    Spinner category;
    String myJSON;
    ArrayList<String> categorylist;
    ImageView proceedBtn;
    ProgressDialog progressDialog;
    ProductDTO productDTO = null;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN= {R.drawable.image_slide5,R.drawable.image_slide2,R.drawable.imageslid3,R.drawable.image_slide4,R.drawable.image_slide5};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();

    DrawerLayout drawer_layout;
    LinearLayout slidMenuLayout;
    TextView rateUsTxt, offerTxt, rewardTxt, termsTxt, logoutTxt, orderHistoryTxt, homeTxt;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        init();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        progressDialog = new ProgressDialog(ProductActivity.this);
        category = (Spinner) findViewById(R.id.material_spinner1);
        productDTO = new ProductDTO();
        proceedBtn = (ImageView) findViewById(R.id.proceedBtn);
        product_grid = (GridView)findViewById(R.id.product_grid);

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), OrderDetailActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

               for(int i=0;i<AppUtils.productList.size();i++){
                   if(AppUtils.productList.get(i).getCustomer_quantity().equals("0")){

                        if(AppUtils.productOrderList.contains(AppUtils.productList.get(i).getProduct_name())){
                            AppUtils.productOrderList.add(AppUtils.productList.get(i));
                            Log.e("",""+AppUtils.productList.get(i).getProduct_name());
                        }
                   }
               }


            }
        });

        if (AppUtils.getInstance(ProductActivity.this).isNetworkConnected()) {
            fetchCategoryFromServer();
            getallproduct();
        } else {
            Toast.makeText(ProductActivity.this, "Please connect to internet to continue!", Toast.LENGTH_LONG).show();
        }

    }




    private void init() {
        sharedPreferences = this.getSharedPreferences("login_status", Context.MODE_PRIVATE);
        rateUsTxt = (TextView)findViewById(R.id.rateUsTxt);
        offerTxt = (TextView)findViewById(R.id.offerTxt);
        rewardTxt = (TextView)findViewById(R.id.rewardTxt);
        termsTxt = (TextView)findViewById(R.id.termsTxt);
        logoutTxt = (TextView)findViewById(R.id.logoutTxt);
        orderHistoryTxt = (TextView)findViewById(R.id.orderHistoryTxt);
        homeTxt = (TextView)findViewById(R.id.homeTxt);

        slidMenuLayout = (LinearLayout)findViewById(R.id.slidMenuLayout);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        for(int i=0;i<XMEN.length;i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ImageSliderAdapter(ProductActivity.this,XMENArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3500, 3500);


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
            }
        });

        rateUsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawers();
                Toast.makeText(getApplicationContext(),"Coming Soon",Toast.LENGTH_SHORT).show();
            }
        });

        offerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Coming Soon",Toast.LENGTH_SHORT).show();
                drawer_layout.closeDrawers();
            }
        });

        rewardTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Coming Soon",Toast.LENGTH_SHORT).show();
                drawer_layout.closeDrawers();
            }
        });

        termsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Coming Soon",Toast.LENGTH_SHORT).show();
                drawer_layout.closeDrawers();
            }
        });

        logoutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawers();
                sharedPreferences =  ProductActivity.this.getSharedPreferences("login_status",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(ProductActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        orderHistoryTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawers();
                Intent intent = new Intent(ProductActivity.this, HistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }
    public void fetchCategoryFromServer() {

        new FetchCategoryTask().execute();
    }




class FetchCategoryTask extends AsyncTask<Void, Void, String> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            String json = WebServiceUtil.getInstance(getApplicationContext()).getJsonFromGetMethod(URLConstant.URL_CATEGORY);
            if (json != null) {
                return json;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (null != result) {
            try {
                final ProductCategory productCategory = new Gson().fromJson(result, ProductCategory.class);
                if (null != productCategory && productCategory.getSuccess() == 1) {
                    Log.d("StateHolder", "" + productCategory.toString());
                    categorylist = new ArrayList<>();
                    categorylist.add(0, "All Categories");
                    for (ProductCategoryData productCategoryData : productCategory.getData()) {
                        if (!productCategoryData.getCategoryName().isEmpty()) {
                            categorylist.add(productCategoryData.getCategoryName());
                        }
                    }

                    if (categorylist.size() > 0) {
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProductActivity.this, android.R.layout.simple_list_item_1, categorylist);
                        Log.d("onItemSelected", "setAdapter for state");
                        category.setAdapter(arrayAdapter);
                        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (position != 0) {
                                    getproductcategorywise(position);
                                } else {
                                    getallproduct();
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }

                } else {
                    Toast.makeText(ProductActivity.this, "Something not right", Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                Toast.makeText(ProductActivity.this, "Something not right", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ProductActivity.this, "Something not right", Toast.LENGTH_SHORT).show();
        }
    }
}

    private void getallproduct() {
        final ProgressDialog loading = ProgressDialog.show(ProductActivity.this, "Getting Details", "Please wait", false, false);
        class AllProductTask extends AsyncTask<String, Void, String> {
            public void onPreExecute() {

            }

            @Override
            protected String doInBackground(String... params) {

                InputStream inputStream = null;
                String result = null;
                try {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.apply();

                    URL url = new URL(URLConstant.URL_ALLPRODUCT);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(15000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder("");
                        String line = "";
                        while ((line = in.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        result = sb.toString();
                    }

                    assert inputStream != null;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    Log.i("tagconvertstr", "[" + result + "]");
                    System.out.println(e);
                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                    } catch (Exception squish) {
                    }
                }
                return result;

            }

            @Override
            protected void onPostExecute(String result) {
                loading.dismiss();
                myJSON = result;
                detail();

            }


        }
        AllProductTask g = new AllProductTask();
        g.execute();
    }

    public void detail() {


        JSONObject json_data = null;

        AppUtils.productList.clear();
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            JSONArray jArray = jsonObj.getJSONArray("data");
            Log.d("jarray",jArray.toString());
            for(int i=0; i<jArray.length(); i++){
                json_data = jArray.getJSONObject(i);
                productDTO = new ProductDTO();

                productDTO.setId(json_data.getString("id"));
                productDTO.setCat_id(json_data.getString("cat_id"));
                productDTO.setProduct_name(json_data.getString("product_name"));
                productDTO.setProduct_image(json_data.getString("product_image"));
                productDTO.setPrice(json_data.getString("price"));
                productDTO.setQuantity(json_data.getString("quantity"));
                productDTO.setDiscription(json_data.getString("discription"));
                productDTO.setCustomer_quantity("");

                AppUtils.productList.add(productDTO);
            }

            productGridAdapter = new ProductGridAdapter(ProductActivity.this,ProductActivity.this, AppUtils.productList);
            product_grid.setAdapter(productGridAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getproductcategorywise(int position) {
        new CategoryWiseProductTask(URLConstant.URL_CATEGORYWISE, position).execute();
    }

    public void onCategorywiseProduct(String categoryWiseProduct) {

        JSONObject json_data = null;

        AppUtils.productList.clear();
        try {
            JSONObject jsonObj = new JSONObject(categoryWiseProduct);
            JSONArray jArray = jsonObj.getJSONArray("data");
            Log.d("jarray",jArray.toString());
            for(int i=0; i<jArray.length(); i++){
                json_data = jArray.getJSONObject(i);
                productDTO = new ProductDTO();

                productDTO.setId(json_data.getString("id"));
                productDTO.setCat_id(json_data.getString("cat_id"));
                productDTO.setProduct_name(json_data.getString("product_name"));
                productDTO.setProduct_image(json_data.getString("product_image"));
                productDTO.setPrice(json_data.getString("price"));
                productDTO.setQuantity(json_data.getString("quantity"));
                productDTO.setDiscription(json_data.getString("discription"));
                productDTO.setCustomer_quantity("");

                AppUtils.productList.add(productDTO);
            }

            productGridAdapter = new ProductGridAdapter(ProductActivity.this,ProductActivity.this, AppUtils.productList);
            product_grid.setAdapter(productGridAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void onFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }
    private class CategoryWiseProductTask extends AsyncTask<String, String, String> {
        String url;
        int position;

        CategoryWiseProductTask(String url, int position) {
            this.url = url;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Fetching Product...");
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
                    .add(AppConstants.CAT_ID, String.valueOf(position))
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
                    CategoryWiseProduct categoryWiseProduct = new Gson().fromJson(json, CategoryWiseProduct.class);
                    if (null != categoryWiseProduct) {
                        if (categoryWiseProduct.getSuccess().intValue() == 1) {
                            onCategorywiseProduct(json);
                        } else if (null != categoryWiseProduct.getMessage()) {
                            onFailed(categoryWiseProduct.getMessage());
                        } else {
                            onFailed("Something went wrong");
                        }
                    } else {
                        onFailed("Something went wrong!");
                    }

                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                onFailed("Something went wrong!");

            }
        }
    }


    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


}
