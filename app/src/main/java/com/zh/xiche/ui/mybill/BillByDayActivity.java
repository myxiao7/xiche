package com.zh.xiche.ui.mybill;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.zh.xiche.R;
import com.zh.xiche.adapter.BillDayAdapter;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.entity.BillDayEntity;
import com.zh.xiche.entity.BillDetailEntity;
import com.zh.xiche.entity.JsonModel;
import com.zh.xiche.entity.UserInfoEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.GsonUtil;
import com.zh.xiche.utils.ToastUtil;
import com.zh.xiche.view.xlistview.XListView;

import org.xutils.http.RequestParams;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 日账单
 * Created by zhanghao on 2016/10/12.
 */

public class BillByDayActivity extends BaseActivity {
    @Bind(R.id.toolbar_tv)
    TextView toolbarTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.xlistview)
    XListView xlistview;

    private UserInfoEntity entity;
    private BillDayAdapter adapetr;
    private List<BillDetailEntity> list = new ArrayList<>();

    private String day;

    private int page = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billbyday);
        ButterKnife.bind(this);
        init();

        getBillByYear(true, day);
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
        day = this.getIntent().getStringExtra("day");
        toolbarTv.setText(day + "账单");
        entity = DbUtils.getInstance().getPersonInfo();

        xlistview.setPullLoadEnable(false);
        xlistview.setPullRefreshEnable(true);

        xlistview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                xlistview.setEnabled(false);
                xlistview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getBillByYear(true, day);
                    }
                },800);
            }

            @Override
            public void onLoadMore() {
                getBillByYear(false, day);
            }
        });
    }

    /**
     * 获取该日期下所有账单
     */
    private void getBillByYear(final boolean isRefresh, String day) {
        if(isRefresh){
            page = 1;
        }else{
            page ++;
        }
        String path = HttpPath.getPath(HttpPath.BILLBYDAY);
        RequestParams params = HttpUtil.params(path);
        params.addBodyParameter("uid", entity.getId());
        params.addBodyParameter("tockens", entity.getTockens());
        params.addBodyParameter("rows", "10");
        params.addBodyParameter("page", page+"");
        params.addBodyParameter("sidx", "");
        params.addBodyParameter("sord", "");
        params.addBodyParameter("dayDate", day);
        HttpUtil.http().post(params, new RequestCallBack<String>(activity) {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Type type = new TypeToken<JsonModel<List<BillDetailEntity>>>() {
                }.getType();
                JsonModel<List<BillDetailEntity>> jsonModel = GsonUtil.GsonToBean(result, type);
                if (jsonModel.isSuccess()) {
                    if (jsonModel.hasData()) {
                        if(isRefresh){
                            list.clear();
                        }
                        list.addAll(jsonModel.getDataList());
                        adapetr = new BillDayAdapter(activity, list);
                        xlistview.setAdapter(adapetr);
                        adapetr.notifyDataSetChanged();
                    } else {
                        if(isRefresh){
                            ToastUtil.showShort("无数据");
                        }else {
                            ToastUtil.showShort("没有更多了");
                        }
                    }
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    xlistview.setRefreshTime(format.format(new Date()));
                    if(list.size() >= 10){
                        xlistview.setPullLoadEnable(true);
                    }
                } else {
                    ToastUtil.showShort("请求失败");
                }
                xlistview.setEnabled(true);
                xlistview.stopRefresh();
                xlistview.stopLoadMore();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
//                ToastUtil.showShort(ex.getMessage());
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
