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
import com.zh.xiche.entity.BillDayEntity;
import com.zh.xiche.entity.BillMonthEntity;
import com.zh.xiche.entity.JsonModel;
import com.zh.xiche.entity.UserInfoEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.GsonUtil;
import com.zh.xiche.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 日账单
 * Created by zhanghao on 2016/10/12.
 */

public class BillByMonthActivity extends BaseActivity {
    @Bind(R.id.toolbar_tv)
    TextView toolbarTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.listview)
    ListView listview;


    private UserInfoEntity entity;
    private BillListAdapetr<BillDayEntity> adapetr;//今年的所有月份账单
    private String month;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billbyyear);
        ButterKnife.bind(this);
        init();

        getBillByYear(month);
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
        month = this.getIntent().getStringExtra("month");
        toolbarTv.setText(month + "账单");
        entity = DbUtils.getInstance().getPersonInfo();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, BillByMonthActivity.class);
                intent.putExtra("day", ((BillDayEntity)parent.getAdapter().getItem(position)).getDaydate());
            }
        });
    }

    /**
     * 获取所有月份账单
     */
    private void getBillByYear(String year) {
        String path = HttpPath.getPath(HttpPath.BILLBYYEAR);
        RequestParams params = HttpUtil.params(path);
        params.addBodyParameter("uid", entity.getId());
        params.addBodyParameter("tockens", entity.getTockens());
        params.addBodyParameter("rows", "10");
        params.addBodyParameter("page", "1");
        params.addBodyParameter("sidx", "");
        params.addBodyParameter("sord", "");
        params.addBodyParameter("monthDate", year);
        HttpUtil.http().post(params, new RequestCallBack<String>(activity) {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                result = "{\n" +
                        "    \"dataList\":[\n" +
                        "        {\n" +
                        "            \"daydate\":\"2016-09-24\",\n" +
                        "            \"dayincome\":400\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"daydate\":\"2016-09-23\",\n" +
                        "            \"dayincome\":300\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"daydate\":\"2016-09-22\",\n" +
                        "            \"dayincome\":200\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"daydate\":\"2016-09-21\",\n" +
                        "            \"dayincome\":100\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"message\":true,\n" +
                        "    \"page\":1,\n" +
                        "    \"record\":4,\n" +
                        "    \"rows\":10,\n" +
                        "    \"total\":1\n" +
                        "}";
                Type type = new TypeToken<JsonModel<List<BillDayEntity>>>() {
                }.getType();
                JsonModel<List<BillDayEntity>> jsonModel = GsonUtil.GsonToBean(result, type);
                if (jsonModel.isSuccess()) {
                    if (jsonModel.hasData()) {
                        adapetr = new BillListAdapetr<>(activity, jsonModel.getDataList());
                        listview.setAdapter(adapetr);
                        adapetr.notifyDataSetChanged();
                    } else {
                        ToastUtil.showShort("无数据");
                    }
                } else {
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