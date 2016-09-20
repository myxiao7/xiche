package com.zh.xiche.ui;

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
 * 忘记密码
 * Created by win7 on 2016/9/20.
 */

public class ForgetActivity extends BaseActivity {
    @Bind(R.id.toolbar_tv)
    TextView toolbarTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.forget_name_edit)
    EditText forgetNameEdit;
    @Bind(R.id.forget_code_edit)
    EditText forgetCodeEdit;
    @Bind(R.id.forget_getcode_txt)
    TextView forgetGetcodeTxt;
    @Bind(R.id.forget_pwd_edit)
    EditText forgetPwdEdit;
    @Bind(R.id.forget_pwd2_edit)
    EditText forgetPwd2Edit;
    @Bind(R.id.forget_btn)
    Button forgetBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
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
        toolbarTv.setText("找回密码");
    }

    @OnClick({R.id.forget_getcode_txt, R.id.forget_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_getcode_txt:
                break;

            case R.id.forget_btn:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
