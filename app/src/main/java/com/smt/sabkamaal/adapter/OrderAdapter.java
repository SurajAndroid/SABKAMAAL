package com.smt.sabkamaal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smt.sabkamaal.R;
import com.smt.sabkamaal.dto.ProductDTO;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by shubham on 3/8/17.
 */

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<ProductDTO> data= Collections.emptyList();

    public OrderAdapter(Context context, List<ProductDTO> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.product_order, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder= (MyHolder) holder;
        myHolder.name.setText(data.get(position).getProduct_name());
        myHolder.Qty.setText(data.get(position).getCustomer_quantity());
        myHolder.Rate.setText(data.get(position).getPrice());
        myHolder.Amt.setText(data.get(position).getTotal_price());
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }



    class MyHolder extends RecyclerView.ViewHolder{

        TextView name,Qty,Rate,Amt;
        EditText qty;
        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            Qty = (TextView) itemView.findViewById(R.id.Qty);
            Rate = (TextView)itemView.findViewById(R.id.Rate);
            Amt = (TextView)itemView.findViewById(R.id.Amt);
        }

    }

}