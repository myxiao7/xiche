package com.zh.xiche.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.zh.xiche.R;
import com.zh.xiche.adapter.MyOrderAdapter;
import com.zh.xiche.adapter.OrderListAdapter;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.entity.OrderEntity;
import com.zh.xiche.entity.OrderResultEntity;
import com.zh.xiche.entity.UserInfoEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.GsonUtil;
import com.zh.xiche.utils.ToastUtil;
import com.zh.xiche.view.xlistview.XListView;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 待接单列表
 * Created by win7 on 2016/10/11.
 */

public class OrderListActivity extends BaseActivity {
    @Bind(R.id.xlistview)
    XListView xlistview;
    @Bind(R.id.toolbar_tv)
    TextView toolbarTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private List<OrderEntity> list = new ArrayList<>();
    private OrderListAdapter adapter;

    private UserInfoEntity entity;
    private int pageIndex = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);
        init();
        xlistview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                xlistview.setEnabled(false);

                xlistview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getOrderList(true);
                    }
                },800);
            }

            @Override
            public void onLoadMore() {
                xlistview.setEnabled(false);
                getOrderList(false);
            }
        });

        xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(activity, OrderDetailsActivity.class);
                intent.putExtra("order", list.get(i-1));
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
        getOrderList(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        xlistview.setEnabled(false);
        getOrderList(true);
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
        toolbarTv.setText("订单列表");

        entity = DbUtils.getInstance().getPersonInfo();
        adapter = new OrderListAdapter(activity, list);
        xlistview.setAdapter(adapter);

        xlistview.setPullLoadEnable(false);
        xlistview.setPullRefreshEnable(true);
    }

    /**
     * 获取订单
     */
    private void getOrderList(final boolean isRefresh){
        if(isRefresh){
            pageIndex = 1;
        }else{
            pageIndex++;
        }
        String path = HttpPath.getPath(HttpPath.LISTWAIT);
        RequestParams params = HttpUtil.params(path);
        params.addBodyParameter("uid", entity.getId());
        params.addBodyParameter("tockens", entity.getTockens());
        params.addBodyParameter("rows", "10");
        params.addBodyParameter("page", pageIndex+"");
        params.addBodyParameter("sidx", "");
        params.addBodyParameter("sord", "");
        HttpUtil.http().post(params, new RequestCallBack<String>(activity){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);
                Type type = new TypeToken<OrderResultEntity>(){}.getType();
                OrderResultEntity orderResultEntity = GsonUtil.GsonToBean(result, type);
                if(orderResultEntity.isSuccee()){
                    if(isRefresh){
                        list.clear();
                        if(orderResultEntity.getDataList().size() > 0){
                            list = orderResultEntity.getDataList();
                            adapter = new OrderListAdapter(activity, list);
                            xlistview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }else{
                            ToastUtil.showShort("没有数据");
                        }
                        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
                        String time = dateFormat.format(new Date());
                        xlistview.setRefreshTime(time);
                        if(list.size() >= 10){
                            xlistview.setPullLoadEnable(true);
                        }
                    }else{
                        if(orderResultEntity.getDataList().size() > 0){
                            list.addAll(orderResultEntity.getDataList());
                            adapter.notifyDataSetChanged();
                        }else{
                            ToastUtil.showShort("没有更多了");
                        }
                    }

                }else{
                    ToastUtil.showShort("获取失败");
                }
                xlistview.setEnabled(true);
                xlistview.stopRefresh();
                xlistview.stopLoadMore();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                ToastUtil.showShort(ex.getMessage());
                xlistview.setEnabled(true);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
