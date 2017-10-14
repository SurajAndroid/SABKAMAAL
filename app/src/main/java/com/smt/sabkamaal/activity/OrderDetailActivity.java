package com.smt.sabkamaal.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qintong.library.InsLoadingView;
import com.smt.sabkamaal.R;
import com.smt.sabkamaal.adapter.AllProductAdapter;
import com.smt.sabkamaal.adapter.OrderAdapter;
import com.smt.sabkamaal.adapter.OrderListAdapter;
import com.smt.sabkamaal.dto.ProductDTO;
import com.smt.sabkamaal.util.AppUtils;
import com.smt.sabkamaal.util.Constant;
import com.smt.sabkamaal.util.RequestReceiver;
import com.smt.sabkamaal.util.WebserviceHelper;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity implements RequestReceiver {
    ImageView back;
    TextView proceedBtn;
    RequestReceiver receiver;
    boolean flage ;
    List<ProductDTO> data;
    OrderAdapter mAdapter;
    OrderListAdapter listAdapter;
    TextView totalTxt;
    int total =0;
    ListView listView;
    TextView tv_order_no, tv_customer_name, tv_order_date, tv_transport_detail, tv_dispatch_date, tv_other;
    SharedPreferences sharedPreferences;
    InsLoadingView loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);


        init();
        clickListener();



    }

    public void init(){

        loader = (InsLoadingView)findViewById(R.id.loader);
        loader.setVisibility(View.INVISIBLE);
        receiver = this;
        sharedPreferences = this.getSharedPreferences("login_status", Context.MODE_PRIVATE);
        data = new ArrayList<>();
        back= (ImageView) findViewById(R.id.back);
        proceedBtn  =(TextView) findViewById(R.id.proceedBtn);
        totalTxt = (TextView)findViewById(R.id.totalTxt);
        listView = (ListView)findViewById(R.id.listView);

        tv_order_no = (TextView)findViewById(R.id.tv_order_no);
        tv_customer_name = (TextView)findViewById(R.id.tv_customer_name);
        tv_order_date = (TextView)findViewById(R.id.tv_order_date);
        tv_transport_detail = (TextView)findViewById(R.id.tv_transport_detail);
        tv_dispatch_date = (TextView)findViewById(R.id.tv_dispatch_date);
        tv_other = (TextView)findViewById(R.id.tv_other);


        for(int i=0;i<AppUtils.productOrderList.size();i++){
            if(!AppUtils.productOrderList.get(i).getCustomer_quantity().equals("")){
                data.add(AppUtils.productOrderList.get(i));
                total += Double.parseDouble(AppUtils.productOrderList.get(i).getTotal_price());
            }
        }

        try{
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        for(int i=0;i< AppUtils.productList.size();i++){
            if(!AppUtils.productList.get(i).getCustomer_quantity().equals("")){
                flage = true;
                break;
            }
        }

        if(flage){
            tv_order_no.setText("ORD-"+getRendomOrlderId());
            tv_customer_name.setText(sharedPreferences.getString("full_name","").toUpperCase());
            tv_order_date.setText(""+getDate());
            tv_transport_detail.setText("-----");
            tv_dispatch_date.setText("-----");
            tv_other.setText("-----");
        }else {
            tv_order_no.setText("-----");
            tv_customer_name.setText("-----");
            tv_order_date.setText("-----");
            tv_transport_detail.setText("-----");
            tv_dispatch_date.setText("-----");
            tv_other.setText("-----");
        }


        totalTxt.setText(""+new DecimalFormat("##.##").format(total));

//        Toast.makeText(getApplicationContext(),"Size : "+AppUtils.productOrderList.size(),Toast.LENGTH_SHORT).show();

        listAdapter = new OrderListAdapter(OrderDetailActivity.this,AppUtils.productOrderList);
        listView.setAdapter(listAdapter);
//        createOrder();

    }


    public void createOrder() {
        WebserviceHelper order = new WebserviceHelper(receiver, OrderDetailActivity.this);
        order.setAction(Constant.CREATEORDER);
        order.execute();
        loader.setVisibility(View.VISIBLE);
    }


    public String getRendomOrlderId(){

        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        return String.format("%05d", num);
    }

    public String getDate(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c.getTime());
    }

    public  void clickListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);*/

                finish();
            }
        });

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flage){
                    createOrder();
                }else {
                    Toast.makeText(getApplicationContext(),"Please select product.",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void requestFinished(String[] result) throws Exception {
        if(result[0].equals("1")){
            loader.setVisibility(View.INVISIBLE);
            AppUtils.productOrderList.clear();
            AppUtils.productId.clear();
            AppUtils.productQuntity.clear();
            AlertDialog.Builder builder1 = new AlertDialog.Builder(OrderDetailActivity.this);
            builder1.setMessage("Order Received. \nThank you.!");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            finish();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }else {
            loader.setVisibility(View.INVISIBLE);
        }
    }
}
