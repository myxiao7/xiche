package com.zh.xiche.ui.myorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.zh.xiche.R;
import com.zh.xiche.adapter.MyOrderAdapter;
import com.zh.xiche.base.BaseApplication;
import com.zh.xiche.base.BaseFragment;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.entity.OrderEntity;
import com.zh.xiche.entity.OrderResultEntity;
import com.zh.xiche.entity.UserInfoEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.ui.OrderDetailsActivity;
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
 * 已经服务订单
 * Created by win7 on 2016/9/22.
 */

public class FragmentMyorder02 extends BaseFragment {
    @Bind(R.id.xlistview)
    XListView xlistview;
    private View mView;
    private List<OrderEntity> list = new ArrayList<>();
    private MyOrderAdapter adapter;

    private UserInfoEntity entity;
    private int pageIndex = 1;

    public static FragmentMyorder02 newInstance(int id) {
        FragmentMyorder02 fragmentMyorder02 = new FragmentMyorder02();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        fragmentMyorder02.setArguments(bundle);
        return fragmentMyorder02;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_myorder01, container, false);
        }
        ButterKnife.bind(this, mView);

        init();

        xlistview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                xlistview.setEnabled(false);
                xlistview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFinishOrder(true);
                    }
                },800);
            }

            @Override
            public void onLoadMore() {
                getFinishOrder(false);
            }
        });

        xlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(activity, OrderDetailsActivity.class);
                intent.putExtra("order", list.get(i-1));
                intent.putExtra("type", 3);
                startActivity(intent);
            }
        });
        getFinishOrder(true);
        return mView;
    }

    private void init() {
        ButterKnife.bind(this, mView);
        IntentFilter filter = new IntentFilter(BaseApplication.ORDERFINISH);
        activity.registerReceiver(receiver,filter);
        entity = DbUtils.getInstance().getPersonInfo();

        entity = DbUtils.getInstance().getPersonInfo();
        adapter = new MyOrderAdapter(activity, list, true);
        xlistview.setAdapter(adapter);

        xlistview.setPullLoadEnable(false);
        xlistview.setPullRefreshEnable(true);
    }
    /**
     * 获取已服务订单
     */
    private void getFinishOrder(final boolean isRefresh){
        if(isRefresh){
            pageIndex = 1;
        }else{
            pageIndex++;
        }
        String path = HttpPath.getPath(HttpPath.ORDERLIST_FINISH);
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
                            adapter = new MyOrderAdapter(activity, list, true);
                            xlistview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }else{
//                            ToastUtil.showShort("没有数据");
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
//                ToastUtil.showShort(ex.getMessage());

            }
        });
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(BaseApplication.ORDERFINISH)){
                getFinishOrder(true);
                LogUtil.d("已完成列表刷新");
            }
        }
    };
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        activity.unregisterReceiver(receiver);
    }
}
