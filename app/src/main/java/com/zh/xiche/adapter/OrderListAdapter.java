package com.zh.xiche.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zh.xiche.R;
import com.zh.xiche.entity.OrderEntity;
import com.zh.xiche.utils.ImageLoaderHelper;
import com.zh.xiche.utils.ToastUtil;

import java.util.List;

/**
 * Created by win7 on 2016/9/25.
 */

public class OrderListAdapter extends BaseAdapter {
    private List<OrderEntity> list;
    private Context context;

    public OrderListAdapter(Context context , List<OrderEntity> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, parent, false);
            holder = new ViewHolder();
            holder.typeTv = (TextView) convertView.findViewById(R.id.order_list_item_type_tv);
            holder.addTv = (TextView) convertView.findViewById(R.id.order_list_item_add_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.order_list_item_time_tv);
            holder.carImg = (ImageView) convertView.findViewById(R.id.order_list_item_car_img);
            holder.carTv = (TextView) convertView.findViewById(R.id.order_list_item_car_tv);
            holder.priceTv = (TextView) convertView.findViewById(R.id.order_list_item_price_tv);
            holder.getTv = (TextView) convertView.findViewById(R.id.order_list_item_get_tv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        OrderEntity entity = list.get(position);

        if (!TextUtils.isEmpty(entity.getServicetypename())) {
            //洗车类型
            holder.typeTv.setText(entity.getServicetypename());
        }
        if (!TextUtils.isEmpty(entity.getLocation())) {
            //地址
            holder.addTv.setText(entity.getLocation());
        }
        if (!TextUtils.isEmpty(entity.getAppointment())) {
            //预约时间
            holder.timeTv.setText(entity.getAppointment());
        }
        if (!TextUtils.isEmpty(entity.getCarbrank())) {
            //车型
            holder.carTv.setText(entity.getCarbrank());
        }
        if (!TextUtils.isEmpty(entity.getOrderamount() + "")) {
            //价格
            holder.priceTv.setText("￥" + entity.getOrderamount() + "");
        }
        /*holder.getTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showShort("抢单");
            }
        });*/
        return convertView;
    }

    class ViewHolder{
        TextView typeTv;//类型
        TextView addTv;//地址
        TextView timeTv;//时间
        ImageView carImg;//车标
        TextView carTv;//车型
        TextView priceTv;//价格
        TextView getTv;//立即抢单
    }
}
