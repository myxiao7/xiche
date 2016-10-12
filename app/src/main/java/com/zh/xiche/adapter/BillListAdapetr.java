package com.zh.xiche.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zh.xiche.R;
import com.zh.xiche.entity.BillDayEntity;
import com.zh.xiche.entity.BillMonthEntity;
import com.zh.xiche.entity.BillYearEntity;

import java.util.List;

/**
 * 账单Adapter
 * Created by zhanghao on 2016/10/12.
 */

public class BillListAdapetr<T> extends BaseAdapter {
    private Context context;
    private List<T> list;

    public BillListAdapetr(Context context, List<T> list) {
        this.context = context;
        this.list = list;
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_list_item, parent, false);
            holder = new ViewHolder();
            holder.dateTv = (TextView) convertView.findViewById(R.id.bill_list_item_data);
            holder.moneyTv = (TextView) convertView.findViewById(R.id.bill_list_item_money);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if(list.get(position) instanceof BillDayEntity){
            BillDayEntity entity = (BillDayEntity) list.get(position);
            holder.dateTv.setText(entity.getDaydate() + "  账单");
            holder.moneyTv.setText("￥"+entity.getDayincome());
        }
        if(list.get(position) instanceof BillMonthEntity){
            BillMonthEntity entity = (BillMonthEntity) list.get(position);
            holder.dateTv.setText(entity.getMonthdate() + "  账单");
            holder.moneyTv.setText("￥"+entity.getMonthincome());
        }
        if(list.get(position) instanceof BillYearEntity){
            BillYearEntity entity = (BillYearEntity) list.get(position);
            holder.dateTv.setText(entity.getYeardate() + "  账单");
            holder.moneyTv.setText("￥"+entity.getYearincome());
        }
        return convertView;
    }

    private class ViewHolder{
        private TextView dateTv;//日期
        private TextView moneyTv;//金额
    }
}
