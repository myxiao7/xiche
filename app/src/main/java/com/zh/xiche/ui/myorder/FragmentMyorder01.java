package com.zh.xiche.ui.myorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.zh.xiche.R;
import com.zh.xiche.adapter.MyOrderAdapter;
import com.zh.xiche.base.BaseFragment;
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
 * 服务中订单
 * Created by win7 on 2016/9/22.
 */

public class FragmentMyorder01 extends BaseFragment {
    @Bind(R.id.xlistview)
    XListView xlistview;
    private View mView;
    private List<OrderEntity> list = new ArrayList<>();
    private MyOrderAdapter adapter;

    private UserInfoEntity entity;
    private int pageIndex = 1;

    public static FragmentMyorder01 newInstance(int id) {
        FragmentMyorder01 fragmentMyorder01 = new FragmentMyorder01();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        fragmentMyorder01.setArguments(bundle);
        return fragmentMyorder01;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_myorder01, container, false);
        }
        init();

        xlistview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                getWaitFinish(true);
            }

            @Override
            public void onLoadMore() {
                getWaitFinish(false);
            }
        });

        getWaitFinish(true);
        return mView;
    }

    private void init() {
        ButterKnife.bind(this, mView);
        entity = DbUtils.getInstance().getPersonInfo();
        adapter = new MyOrderAdapter(activity, list, false);
        xlistview.setAdapter(adapter);

        xlistview.setPullLoadEnable(true);
        xlistview.setPullRefreshEnable(true);
    }

    /**
     * 获取已经服务订单
     */
    private void getWaitFinish(final boolean isRefresh){
        if(isRefresh){
            pageIndex = 1;
        }else{
            pageIndex++;
        }
        String path = HttpPath.getPath(HttpPath.ORDERLIST_WAIT);
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
                            adapter = new MyOrderAdapter(activity, list, false);
                            xlistview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
                            String time = dateFormat.format(new Date());
                            xlistview.setRefreshTime(time);
                            ToastUtil.showShort("有数据");
                        }else{
                            ToastUtil.showShort("没有数据");
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
                xlistview.stopRefresh();
                xlistview.stopLoadMore();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                ToastUtil.showShort(ex.getMessage());
                xlistview.stopRefresh();
                xlistview.stopLoadMore();
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
