package com.smt.sabkamaal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.smt.sabkamaal.R;
import com.smt.sabkamaal.constant.Productlist;
import com.smt.sabkamaal.dto.ProductDTO;
import com.smt.sabkamaal.util.AppUtils;
import com.squareup.picasso.Picasso;


import java.util.Collections;
import java.util.List;

/**
 * Created by shubham on 3/8/17.
 */

public class AllProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<ProductDTO> data= Collections.emptyList();

    public AllProductAdapter(Context context, List<ProductDTO> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.product_container, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // Get current position of item in recyclerview to bind data and assign values from list

        final MyHolder myHolder= (MyHolder) holder;
        final ProductDTO current=data.get(position);

        myHolder.productname.setText(current.getProduct_name());
        myHolder.price.setText(current.getPrice());


        Picasso.with(context)
                .load(current.getProduct_image())
                .into(myHolder.ivFish);

        myHolder.qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    if(s.toString().length()!=0){
                        if(Integer.parseInt(current.getQuantity())>=Integer.parseInt(s.toString())){
                            data.get(position).setCustomer_quantity(""+s.toString());
                            AppUtils.productList.get(position).setCustomer_quantity(""+s.toString());
                            data.get(position).setTotal_price(""+(Integer.parseInt(data.get(position).getPrice())*(Integer.parseInt(s.toString()))));
                            myHolder.totalprice.setText(""+(Integer.parseInt(data.get(position).getPrice())*(Integer.parseInt(s.toString()))));
                        }else {
                            Toast.makeText(context,"Product is over. Select less quantity",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        myHolder.totalprice.setText("");
                        AppUtils.productList.get(position).setCustomer_quantity("");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    AppUtils.productList.get(position).setCustomer_quantity("");
                }
            }
        });

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }



    class MyHolder extends RecyclerView.ViewHolder{
        ImageView ivFish;
        TextView productname,price,totalprice;
        EditText qty;
        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            productname = (TextView) itemView.findViewById(R.id.names);
            ivFish = (ImageView) itemView.findViewById(R.id.ivFish_banner);
            totalprice = (TextView)itemView.findViewById(R.id.totalprice);
            price = (TextView)itemView.findViewById(R.id.price);
            qty = (EditText)itemView.findViewById(R.id.qty);
        }

    }

}