package com.zh.xiche.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zh.xiche.R;
import com.zh.xiche.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2016/9/27.
 */

public class RegisterUserInfoActivity extends BaseActivity {
    @Bind(R.id.toolbar_tv)
    TextView toolbarTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.register_notice_tv)
    TextView registerNoticeTv;
    @Bind(R.id.register_citv_tv)
    TextView registerCitvTv;
    @Bind(R.id.register_name_edit)
    EditText registerNameEdit;
    @Bind(R.id.register_card_edit)
    EditText registerCardEdit;
    @Bind(R.id.register_registe_btn)
    Button registerRegisteBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_userinfo);
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

    @OnClick({R.id.register_citv_tv, R.id.register_registe_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_citv_tv:
                Intent intent = new Intent(activity, SelectCityActivy.class);
                startActivity(intent);
                break;

            case R.id.register_registe_btn:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
