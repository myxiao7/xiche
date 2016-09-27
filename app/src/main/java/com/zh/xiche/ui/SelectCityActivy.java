package com.zh.xiche.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zh.xiche.R;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.view.slider.SideBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by win7 on 2016/9/28.
 */

public class SelectCityActivy extends BaseActivity {
    @Bind(R.id.toolbar_tv)
    TextView toolbarTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.selectcity_listview)
    ListView selectcityListview;
    @Bind(R.id.selectcity_dialog)
    TextView selectcityDialog;
    @Bind(R.id.sidrbar)
    SideBar sidrbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcity);
        init();
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
        toolbarTv.setText("用户信息");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
