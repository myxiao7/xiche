package com.zh.xiche.ui.myorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zh.xiche.R;
import com.zh.xiche.adapter.MyOrderAdapter;
import com.zh.xiche.base.BaseFragment;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.entity.OrderEntity;
import com.zh.xiche.entity.UserInfoEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.view.xlistview.XListView;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 待服务订单
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

        entity = DbUtils.getInstance().getPersonInfo();
        getWaitFinish(false);

       /* for (int i = 0; i < 10; i++) {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setId(i);
            orderEntity.setName(i+"name");
            list.add(orderEntity);
        }*/
        adapter = new MyOrderAdapter(activity, list, true);
        xlistview.setAdapter(adapter);
        xlistview.setPullLoadEnable(true);
        xlistview.setPullRefreshEnable(true);
        xlistview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                getWaitFinish(false);
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
                String time = dateFormat.format(new Date());
                xlistview.setRefreshTime(time);
                xlistview.stopRefresh();
                xlistview.stopLoadMore();
            }

            @Override
            public void onLoadMore() {
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setName("加载更多"+"name");
                list.add(orderEntity);
                adapter.notifyDataSetChanged();
                xlistview.stopRefresh();
                xlistview.stopLoadMore();
            }
        });
        return mView;
    }

    /**
     * 获取已经服务订单
     */
    private void getWaitFinish(boolean isRefresh){
        if(isRefresh){
            pageIndex = 1;
        }else{
            pageIndex++;
        }
        String path = HttpPath.getPath(HttpPath.ORDERLIST_WAIT);
        RequestParams params = HttpUtil.params(path);
        params.addBodyParameter("operid", entity.getId());
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
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
