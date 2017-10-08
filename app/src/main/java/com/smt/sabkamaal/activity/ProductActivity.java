package com.smt.sabkamaal.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.smt.sabkamaal.R;
import com.smt.sabkamaal.adapter.AllProductAdapter;
import com.smt.sabkamaal.adapter.ProductGridAdapter;
import com.smt.sabkamaal.constant.AppConstants;
import com.smt.sabkamaal.constant.Productlist;
import com.smt.sabkamaal.constant.URLConstant;
import com.smt.sabkamaal.dto.ProductDTO;
import com.smt.sabkamaal.holder.ProductCategory;
import com.smt.sabkamaal.holder.ProductCategoryData;
import com.smt.sabkamaal.util.AppUtils;
import com.smt.sabkamaal.webService.WebServiceUtil;


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

import javax.net.ssl.HttpsURLConnection;

public class ProductActivity extends AppCompatActivity {

    private AllProductAdapter mAdapter;
    private ProductGridAdapter productGridAdapter;
    GridView  product_grid;
    Spinner materialBetterSpinner ;
    String myJSON,catid;
    ArrayList<String> statelist;
    ImageView proceedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        materialBetterSpinner = (Spinner) findViewById(R.id.material_spinner1);
        proceedBtn = (ImageView) findViewById(R.id.proceedBtn);
        product_grid = (GridView)findViewById(R.id.product_grid);

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), OrderDetailActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });

        if (AppUtils.getInstance(ProductActivity.this).isNetworkConnected()) {
            fetchCategoryFromServer();
            getTechnicalUpdate();
        } else {
            Toast.makeText(ProductActivity.this, "Please connect to internet to continue!", Toast.LENGTH_LONG).show();
        }

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

                    final ProductCategory stateHolder = new Gson().fromJson(result, ProductCategory.class);
                    if (null != stateHolder && stateHolder.getSuccess() == 1) {
                        Log.d("StateHolder", "" + stateHolder.toString());
                        statelist = new ArrayList<>();
                        for (ProductCategoryData stateDeatailHolder : stateHolder.getData()) {
                            if (!stateDeatailHolder.getCategoryName().isEmpty()) {
                                statelist.add(stateDeatailHolder.getCategoryName());
                            }
                        }
                        if (statelist.size() > 0) {
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ProductActivity.this,android.R.layout.simple_dropdown_item_1line, statelist);
                            Log.d("onItemSelected", "setAdapter for state");
                            materialBetterSpinner.setAdapter(arrayAdapter);
                            materialBetterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (parent.getId() == R.id.material_spinner1){
                                        getproductcategorywise();
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
    private void getTechnicalUpdate() {
        final ProgressDialog loading = ProgressDialog.show(ProductActivity.this, "Getting Details", "Please wait", false, false);

        class TechnicalUpdateTask extends AsyncTask<String, Void, String> {
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

                    int responseCode=conn.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder("");
                        String line="";
                        while ((line = in.readLine()) != null)
                        {
                            sb.append(line).append("\n");
                        }
                        result = sb.toString();
                    }

                    assert inputStream != null;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line).append("\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    Log.i("tagconvertstr", "["+result+"]");
                    System.out.println(e);
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                loading.dismiss();
                myJSON = result;
                detail();
            }
        }
        TechnicalUpdateTask g = new TechnicalUpdateTask();
        g.execute();
    }


    public void detail(){

        List<Productlist> technicaldata =new ArrayList<>();
        technicaldata.clear();
        JSONObject json_data = null;
        ProductDTO productDTO = null;
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
          private void getproductcategorywise(){
              final ProgressDialog loading = ProgressDialog.show(ProductActivity.this, "Getting Details", "Please wait", false, false);

              class TechnicalUpdateTask extends AsyncTask<String, Void, String> {
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

                          URL url = new URL(URLConstant.URL_CATEGORYWISE);

                          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                          conn.setReadTimeout(15000 /* milliseconds */);
                          conn.setConnectTimeout(15000 /* milliseconds */);
                          conn.setRequestMethod("POST");
                          conn.addRequestProperty("cat_id","4");
                          conn.setDoInput(true);
                          conn.setDoOutput(true);
                          OutputStream os = conn.getOutputStream();

                          BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                         /* BufferedWriter writer = new BufferedWriter(
                                  new OutputStreamWriter(os, "UTF-8"));
                          writer.write(getPostDataString(postDataParams));*/

                          writer.flush();
                          writer.close();
                          os.close();

                          int responseCode=conn.getResponseCode();

                          if (responseCode == HttpsURLConnection.HTTP_OK) {

                              BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                              StringBuilder sb = new StringBuilder("");
                              String line="";
                              while ((line = in.readLine()) != null)
                              {
                                  sb.append(line).append("\n");
                              }
                              result = sb.toString();
                          }

                          assert inputStream != null;
                          BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                          StringBuilder sb = new StringBuilder();

                          String line = null;
                          while ((line = reader.readLine()) != null)
                          {
                              sb.append(line).append("\n");
                          }
                          result = sb.toString();
                      } catch (Exception e) {
                          Log.i("tagconvertstr", "["+result+"]");
                          System.out.println(e);
                      }
                      finally {
                          try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                      }
                      return result;

                  }

                  @Override
                  protected void onPostExecute(String result){
                      loading.dismiss();
                      myJSON = result;
//                      detail1();
                  }
              }
              TechnicalUpdateTask g = new TechnicalUpdateTask();
              g.execute();
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

  /*  public void detail1(){

        List<Productlist> technicaldata =new ArrayList<>();
        technicaldata.clear();
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
//
//                    // Getting JSON Array node
//                    JSONArray data = jsonObj.getJSONArray("data");
            JSONArray jArray = jsonObj.getJSONArray("data");
            Log.d("jarray",jArray.toString());
            for(int i=0; i<jArray.length(); i++){
                JSONObject json_data = jArray.getJSONObject(i);
                Log.d("jsondata",json_data.toString());
                Productlist fishData = new Productlist();
                fishData.product_name = json_data.getString("product_name");

                fishData.price = json_data.getString("price");

                String img= json_data.getString("product_image");
                String[] gmi = img.split(",");
                fishData.product_image = gmi[0];
                // fishData.blogs_desc = json_data.getString("blogs_desc");
                Log.d("fishdata",fishData.toString());
                technicaldata.add(fishData);
            }
            recyclerView=(RecyclerView)findViewById(R.id.my_recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            mAdapter=new AllProductAdapter(getApplicationContext(),technicaldata);
            recyclerView.setAdapter(mAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
}
