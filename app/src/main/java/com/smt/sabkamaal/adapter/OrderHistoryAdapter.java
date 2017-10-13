package com.smt.sabkamaal.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.smt.sabkamaal.R;
import com.smt.sabkamaal.dto.HistoryDTO;
import com.smt.sabkamaal.dto.ProductDTO;

import java.util.Collections;
import java.util.List;

/**
 * Created by Suraj shakya on 11/8/16.
 * shakyasuraj08@mail.com
 */


public class OrderHistoryAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    Context context;
    Activity activity;
    List<HistoryDTO> data= Collections.emptyList();

    public OrderHistoryAdapter(Context context, List<HistoryDTO> data){
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
          convertView = layoutInflater.inflate(R.layout.history_order,null);

            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.Qty = (TextView) convertView.findViewById(R.id.Qty);
            holder.Rate = (TextView)convertView.findViewById(R.id.Rate);
            holder.Amt = (TextView)convertView.findViewById(R.id.Amt);
            holder.dateTimeTxt = (TextView)convertView.findViewById(R.id.dateTimeTxt);
            holder.orderidTxt = (TextView)convertView.findViewById(R.id.orderidTxt);

          convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(data.get(position).getProduct_name());
        holder.Qty.setText(data.get(position).getQty());
        holder.Rate.setText(data.get(position).getPrice());
        holder.Amt.setText(data.get(position).getTotal());
        holder.dateTimeTxt.setText(data.get(position).getDate_time());
        holder.orderidTxt.setText("Order Id - "+data.get(position).getOrder_id());

        return convertView;
    }

    class ViewHolder{

        TextView name, Qty, Rate,Amt,dateTimeTxt, orderidTxt;

    }
}
