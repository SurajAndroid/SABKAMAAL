package com.smt.sabkamaal.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.smt.sabkamaal.R;
import com.smt.sabkamaal.activity.ProductDetail;
import com.smt.sabkamaal.dto.ProductDTO;
import com.smt.sabkamaal.util.AppUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by Suraj shakya on 11/8/16.
 * shakyasuraj08@mail.com
 */


public class OrderListAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    Activity activity;
    List<ProductDTO> data= Collections.emptyList();

    public OrderListAdapter(Context context, List<ProductDTO> data){
        this.context = context;
        this.activity = activity;
        this.data=data;
    }
/*

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
*/

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        layoutInflater = LayoutInflater.from(context);
        final ViewHolder holder;
        if(convertView==null){
          holder = new ViewHolder();
          convertView = layoutInflater.inflate(R.layout.product_order,null);

            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.Qty = (TextView) convertView.findViewById(R.id.Qty);
            holder.Rate = (TextView)convertView.findViewById(R.id.Rate);
            holder.Amt = (TextView)convertView.findViewById(R.id.Amt);

          convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(data.get(position).getProduct_name());
        holder.Qty.setText(data.get(position).getCustomer_quantity());
        holder.Rate.setText(data.get(position).getPrice());
        holder.Amt.setText(data.get(position).getTotal_price());

        return convertView;
    }

    class ViewHolder{

        TextView name, Qty, Rate,Amt;
        EditText editText;
        ImageView productImage;
    }
}
