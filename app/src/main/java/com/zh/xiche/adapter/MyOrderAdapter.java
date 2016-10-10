package com.zh.xiche.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zh.xiche.R;
import com.zh.xiche.entity.OrderEntity;
import com.zh.xiche.utils.ToastUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by win7 on 2016/9/25.
 */

public class MyOrderAdapter extends BaseAdapter {
    private List<OrderEntity> list;
    private Context context;
    private boolean isFinish;//区分待服务 已服务

    public MyOrderAdapter(Context context ,List<OrderEntity> list) {
        this.list = list;
        this.context = context;
    }
    public MyOrderAdapter(Context context ,List<OrderEntity> list, boolean finish) {
        this.list = list;
        this.context = context;
        this.isFinish = finish;
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorder_list_item, parent, false);
            holder = new ViewHolder();
            holder.iconImg = (CircleImageView) convertView.findViewById(R.id.myorder_list_item_icon_img);
            holder.nameTv = (TextView) convertView.findViewById(R.id.myorder_list_item_name_tv);
            holder.phoneTv = (TextView) convertView.findViewById(R.id.myorder_list_item_phone_tv);
            holder.phoneBtn = (ImageView) convertView.findViewById(R.id.myorder_list_item_phone_img);
            holder.orderTv = (TextView) convertView.findViewById(R.id.myorder_list_item_order_tv);
            holder.typeTv = (TextView) convertView.findViewById(R.id.myorder_list_item_type_tv);
            holder.addTv = (TextView) convertView.findViewById(R.id.myorder_list_item_add_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.myorder_list_item_time_tv);
            holder.carYupeTv = (TextView) convertView.findViewById(R.id.myorder_list_item_cartype_tv);
            holder.colorTv = (TextView) convertView.findViewById(R.id.myorder_list_item_color_tv);
            holder.numTv = (TextView) convertView.findViewById(R.id.myorder_list_item_color_tv);
            holder.priceTv = (TextView) convertView.findViewById(R.id.myorder_list_item_price_tv);
            holder.dateTv = (TextView) convertView.findViewById(R.id.myorder_list_item_date_tv);
            holder.dateDesTv = (TextView) convertView.findViewById(R.id.myorder_list_item_datedes_tv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(isFinish){
            holder.dateDesTv.setText("完成时间");
        }else{
            holder.dateDesTv.setText("接单时间");
        }
        OrderEntity entity = list.get(position);
        holder.nameTv.setText(entity.getName());
        holder.phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort("打电话");
            }
        });
        return convertView;
    }

    class ViewHolder{
        CircleImageView iconImg;//头像
        TextView nameTv;//姓名
        TextView phoneTv;//电话
        ImageView phoneBtn;//打电话
        TextView orderTv;//订单号
        TextView typeTv;//类型
        TextView addTv;//地址
        TextView timeTv;//时间
        TextView carYupeTv;//车型
        TextView colorTv;//颜色
        TextView numTv;//车牌
        TextView priceTv;//价格
        TextView dateTv;//接单时间
        TextView dateDesTv;//接单描述
    }
}
