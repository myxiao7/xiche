package com.zh.xiche.ui.myorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zh.xiche.R;
import com.zh.xiche.adapter.FragViewPagerAdapter;
import com.zh.xiche.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的订单切换
 * Created by win7 on 2016/9/22.
 */

public class MyOrderSwitch extends BaseActivity {
    @Bind(R.id.toolbar_tv)
    TextView toolbarTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.myorder_viewpager)
    ViewPager myorderViewpager;

    private List<Fragment> list = new ArrayList<>();
    private String []titles = {"待服务", "已服务"};
    private FragViewPagerAdapter adapter;

    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
        init();
        Fragment fragment01 = FragmentMyorder01.newInstance(1);
        Fragment fragment02 = FragmentMyorder02.newInstance(1);
        list.add(fragment01);
        list.add(fragment02);
        adapter = new FragViewPagerAdapter(getSupportFragmentManager(),list,titles);
        myorderViewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(myorderViewpager);
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
        toolbarTv.setText("我的订单");
        //获取查询的用户ID
        id = this.getIntent().getIntExtra("id", 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
