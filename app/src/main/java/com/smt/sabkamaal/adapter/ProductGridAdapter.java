package com.smt.sabkamaal.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.smt.sabkamaal.R;
import com.smt.sabkamaal.activity.ProductDetail;
import com.smt.sabkamaal.dto.ProductDTO;
import com.smt.sabkamaal.util.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Suraj shakya on 11/8/16.
 * shakyasuraj08@mail.com
 */


public class ProductGridAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    Activity activity;
    List<ProductDTO> data= Collections.emptyList();

    public ProductGridAdapter(Context context, Activity activity, List<ProductDTO> data){
        this.context = context;
        this.activity = activity;
        this.data=data;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

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
          convertView = layoutInflater.inflate(R.layout.product_element,null);

          holder.productName = (TextView)convertView.findViewById(R.id.productName);
          holder.priceTxt = (TextView)convertView.findViewById(R.id.priceTxt);
          holder.total_priceTxt = (TextView)convertView.findViewById(R.id.total_priceTxt);
          holder.editText = (EditText)convertView.findViewById(R.id.editText);
          holder.productImage = (ImageView) convertView.findViewById(R.id.productImage);
          holder.hoardvalue = (TextView)convertView.findViewById(R.id.hoardvalue);
          convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ProductDTO current=data.get(position);

        holder.productName.setText(current.getProduct_name());
        holder.priceTxt.setText("Rs."+current.getPrice());

        Glide.with(context)
                .load(current.getProduct_image()).fitCenter()
                .into(holder.productImage);

        holder.editText.addTextChangedListener(new TextWatcher() {
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
                        if(Double.parseDouble(current.getQuantity())>=Double.parseDouble(s.toString())){
                            data.get(position).setCustomer_quantity(""+s.toString());
                            AppUtils.productList.get(position).setCustomer_quantity(""+s.toString());
                            data.get(position).setTotal_price(""+(Double.parseDouble(data.get(position).getPrice())*(Double.parseDouble(s.toString()))));
                            holder.hoardvalue.setText("Rs.");
                            holder.total_priceTxt.setText(""+(Double.parseDouble(data.get(position).getPrice())*(Double.parseDouble(s.toString()))));
                        }else {
                            Toast.makeText(context,"Product is over. Select less quantity",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        holder.total_priceTxt.setText("");
                        holder.hoardvalue.setText("Rs.0.0");
                        AppUtils.productList.get(position).setCustomer_quantity("");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    holder.total_priceTxt.setText("");
                    holder.hoardvalue.setText("Rs.0.0");
                    AppUtils.productList.get(position).setCustomer_quantity("");
                }
            }
        });


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,ProductDetail.class);
                intent.putExtra("position",""+position);
                activity.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder{
        TextView productName, priceTxt, total_priceTxt,hoardvalue;
        EditText editText;
        ImageView productImage;
    }
}
