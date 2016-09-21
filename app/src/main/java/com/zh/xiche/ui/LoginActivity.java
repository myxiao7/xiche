package com.zh.xiche.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Created by win7 on 2016/9/18.
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.login_name_txt)
    EditText loginNameTxt;
    @Bind(R.id.login_pwd_txt)
    EditText loginPwdTxt;
    @Bind(R.id.login_getcode_txt)
    TextView loginGetcodeTxt;
    @Bind(R.id.login_login_btn)
    Button loginLoginBtn;
    @Bind(R.id.login_register_txt)
    TextView loginRegisterTxt;

    private static final int REGISTERCODE = 0x1001;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.login_login_btn, R.id.login_register_txt, R.id.login_getcode_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_login_btn:
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.login_register_txt:
                Intent intent2 = new Intent(activity, RegisterActivity.class);
                startActivityForResult(intent2, REGISTERCODE);
                break;
            case R.id.login_getcode_txt:
                Intent intent3 = new Intent(activity, ForgetActivity.class);
                startActivityForResult(intent3, REGISTERCODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTERCODE && resultCode == RESULT_OK && data != null) {
            loginNameTxt.setText(data.getStringExtra("userName"));
            loginPwdTxt.setText(data.getStringExtra("userPwd"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
