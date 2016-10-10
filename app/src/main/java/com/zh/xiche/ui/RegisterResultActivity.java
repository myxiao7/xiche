package com.zh.xiche.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.zh.xiche.R;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.entity.ResultEntity;
import com.zh.xiche.entity.UserInfoEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.DialogUtils;
import com.zh.xiche.utils.GsonUtil;
import com.zh.xiche.utils.ToastUtil;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册结果
 * Created by win7 on 2016/9/27.
 */

public class RegisterResultActivity extends BaseActivity {


    @Bind(R.id.toolbar_tv)
    TextView toolbarTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.register_result_btn)
    Button registerResultBtn;

    private String userName, userPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_result);
        ButterKnife.bind(this);
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
        toolbarTv.setText("注册结果");
        Intent intent = this.getIntent();
        userName = intent.getStringExtra("userName");
        userPwd = intent.getStringExtra("userPwd");
    }

    /**
     * 登录
     */
    private void Login() {
        String url = HttpPath.getPath(HttpPath.REGISTER);
        RequestParams params = HttpUtil.params(url);
        params.addBodyParameter("mobile", userName);
        params.addBodyParameter("password", userPwd);
        HttpUtil.http().post(params, new RequestCallBack<String>(activity){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                DialogUtils.stopProgress(activity);
                LogUtil.d(result);
                Type type = new TypeToken<ResultEntity>(){}.getType();
                ResultEntity entity = GsonUtil.GsonToBean(result, type);
                if(entity.isSuccee()){
                    ToastUtil.showShort("登录成功");
                    //保存用户信息
                    DbUtils.getInstance().clearPersonInfo();
                    DbUtils.getInstance().savePersonInfo(entity.getOperatorDTO());
                    //去首页
                    Intent intent = new Intent(activity, MainActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtil.showShort("登录失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                DialogUtils.stopProgress(activity);
                ToastUtil.showShort(ex.getMessage());
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.register_result_btn)
    public void onClick() {
        //登录
        Login();
    }
}
