package com.zh.xiche.ui.mybill;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.zh.xiche.R;
import com.zh.xiche.adapter.BillListAdapetr;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.entity.BillMonthEntity;
import com.zh.xiche.entity.BillYearEntity;
import com.zh.xiche.entity.JsonModel;
import com.zh.xiche.entity.UserInfoEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.GsonUtil;
import com.zh.xiche.utils.ListViewUtils;
import com.zh.xiche.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 年账单
 * Created by zhanghao on 2016/10/12.
 */

public class BillByAllActivity extends BaseActivity {
    @Bind(R.id.toolbar_tv)
    TextView toolbarTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.listview01)
    ListView listview01;
    @Bind(R.id.listview02)
    ListView listview02;

    private UserInfoEntity entity;
    private BillListAdapetr<BillMonthEntity> adapetr;//今年的所有月份账单
    private BillListAdapetr<BillYearEntity> adapetr2;//年账单

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billbyall);
        init();
        getBillByAll();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        getBillByYear(year+"");
    }

    private void init() {
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        toolbarTv.setText("我的账单");
        entity = DbUtils.getInstance().getPersonInfo();

        listview01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, BillByMonthActivity.class);
                intent.putExtra("month", ((BillMonthEntity)parent.getAdapter().getItem(position)).getMonthdate());
            }
        });

        listview02.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, BillByYearActivity.class);
                intent.putExtra("year", ((BillYearEntity)parent.getAdapter().getItem(position)).getYeardate());
            }
        });
    }

    /**
     * 获取所有年份账单
     */
    private void getBillByAll(){
        String path = HttpPath.getPath(HttpPath.BILLBYALL);
        RequestParams params = HttpUtil.params(path);
        params.addBodyParameter("uid", entity.getId());
        params.addBodyParameter("tockens", entity.getTockens());
        params.addBodyParameter("rows", "10");
        params.addBodyParameter("page", "1");
        params.addBodyParameter("sidx", "");
        params.addBodyParameter("sord", "");
        HttpUtil.http().post(params, new RequestCallBack<String>(activity){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                result = "{\n" +
                        "    \"dataList\":[\n" +
                        "        {\n" +
                        "            \"yeardate\":\"2016\",\n" +
                        "            \"yearincome\":300\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"yeardate\":\"2015\",\n" +
                        "            \"yearincome\":100\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"yeardate\":\"2014\",\n" +
                        "            \"yearincome\":100\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"yeardate\":\"2013\",\n" +
                        "            \"yearincome\":100\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"message\":true,\n" +
                        "    \"page\":1,\n" +
                        "    \"record\":2,\n" +
                        "    \"rows\":10,\n" +
                        "    \"total\":1\n" +
                        "}";
                Type type = new TypeToken<JsonModel<List<BillYearEntity>>>(){}.getType();
                JsonModel<List<BillYearEntity>> jsonModel = GsonUtil.GsonToBean(result, type);
                if(jsonModel.isSuccess()){
                    if(jsonModel.hasData()){
                        adapetr2 = new BillListAdapetr<>(activity,jsonModel.getDataList());
                        listview02.setAdapter(adapetr2);
                        ListViewUtils.setListViewHeightBasedOnChildren(listview02);
                        adapetr2.notifyDataSetChanged();
                    }else{
                        ToastUtil.showShort("无数据");
                    }
                }else{
                    ToastUtil.showShort("请求失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                ToastUtil.showShort(ex.getMessage());
            }
        });
    }

    /**
     * 获取今年所有月份账单
     */
    private void getBillByYear(String year){
        String path = HttpPath.getPath(HttpPath.BILLBYYEAR);
        RequestParams params = HttpUtil.params(path);
        params.addBodyParameter("uid", entity.getId());
        params.addBodyParameter("tockens", entity.getTockens());
        params.addBodyParameter("rows", "10");
        params.addBodyParameter("page", "1");
        params.addBodyParameter("sidx", "");
        params.addBodyParameter("sord", "");
        params.addBodyParameter("yearDate", year);
        HttpUtil.http().post(params, new RequestCallBack<String>(activity){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                result = "{\n" +
                        "    \"dataList\":[\n" +
                        "        {\n" +
                        "            \"monthdate\":\"2016-10\",\n" +
                        "            \"monthincome\":100\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"monthdate\":\"2016-09\",\n" +
                        "            \"monthincome\":100\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"monthdate\":\"2016-08\",\n" +
                        "            \"monthincome\":100\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"monthdate\":\"2016-07\",\n" +
                        "            \"monthincome\":100\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"monthdate\":\"2016-06\",\n" +
                        "            \"monthincome\":100\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"monthdate\":\"2016-05\",\n" +
                        "            \"monthincome\":100\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"monthdate\":\"2016-04\",\n" +
                        "            \"monthincome\":100\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"monthdate\":\"2016-03\",\n" +
                        "            \"monthincome\":100\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"monthdate\":\"2016-02\",\n" +
                        "            \"monthincome\":100\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"monthdate\":\"2016-01\",\n" +
                        "            \"monthincome\":100\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"message\":true,\n" +
                        "    \"page\":1,\n" +
                        "    \"record\":2,\n" +
                        "    \"rows\":10,\n" +
                        "    \"total\":1\n" +
                        "}";
                Type type = new TypeToken<JsonModel<List<BillMonthEntity>>>(){}.getType();
                JsonModel<List<BillMonthEntity>> jsonModel = GsonUtil.GsonToBean(result, type);
                if(jsonModel.isSuccess()){
                    if(jsonModel.hasData()){
                        adapetr = new BillListAdapetr<>(activity,jsonModel.getDataList());
                        listview01.setAdapter(adapetr);
                        ListViewUtils.setListViewHeightBasedOnChildren(listview01);
                        adapetr.notifyDataSetChanged();
                    }else{
                        ToastUtil.showShort("无数据");
                    }
                }else{
                    ToastUtil.showShort("请求失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                ToastUtil.showShort(ex.getMessage());
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
