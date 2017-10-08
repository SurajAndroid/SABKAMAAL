package com.smt.sabkamaal.activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smt.sabkamaal.R;
import com.smt.sabkamaal.util.AppUtils;

/**
 * Created by SURAJ on 10/8/2017.
 */

public class ProductDetail extends AppCompatActivity {

    LinearLayout slidMenuLayout;
    ImageView productImage;
    TextView productNameTxt, priceTxt, discriptionTxt;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_activity);

        init();
        clickListener();
    }

    public void init(){
        slidMenuLayout = (LinearLayout)findViewById(R.id.slidMenuLayout);

        productImage = (ImageView)findViewById(R.id.productImage);
        productNameTxt = (TextView)findViewById(R.id.productNameTxt);
        priceTxt = (TextView)findViewById(R.id.priceTxt);
        discriptionTxt = (TextView)findViewById(R.id.discriptionTxt);

        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("position")!= null)
        {
            position = Integer.parseInt(bundle.getString("position").toString());
        }

        setDetail();

    }

    public void setDetail(){
        productNameTxt.setText(AppUtils.productList.get(position).getProduct_name());
        priceTxt.setText("Rs."+AppUtils.productList.get(position).getPrice());
        discriptionTxt.setText(AppUtils.productList.get(position).getDiscription());

        try{
            Glide.with(getApplicationContext())
                    .load(AppUtils.productList.get(position).getProduct_image()).fitCenter()
                    .into(productImage);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void clickListener(){

        slidMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}
