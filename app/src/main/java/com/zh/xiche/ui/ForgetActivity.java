package com.zh.xiche.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.zh.xiche.R;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.config.SharedData;
import com.zh.xiche.entity.ResultEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.utils.CommonCheck;
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

    private CountDownTimer downTimer;

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
        toolbarTv.setText("重置密码");
    }

    @OnClick({R.id.forget_getcode_txt, R.id.forget_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_getcode_txt:
                if(TextUtils.isEmpty(forgetNameEdit.getText().toString())){
                    ToastUtil.showShort(R.string.login_phone_hint);
                    return;
                }

                if(!CommonCheck.isMobile(forgetNameEdit.getText().toString())){
                    ToastUtil.showShort(R.string.login_vai_hint);
                    return;
                }
                getCode(forgetNameEdit.getText().toString());
                break;

            case R.id.forget_btn:
                DialogUtils.showProgress(activity);
                if(TextUtils.isEmpty(forgetCodeEdit.getText().toString())){
                    ToastUtil.showShort(R.string.code);
                    return;
                }
                if(TextUtils.isEmpty(forgetPwdEdit.getText().toString())){
                    ToastUtil.showShort(R.string.forget_pwd_hint);
                    return;
                }
                if(TextUtils.isEmpty(forgetPwd2Edit.getText().toString())){
                    ToastUtil.showShort(R.string.forget_pwd_hint2);
                    return;
                }
                if(!(forgetPwd2Edit.getText().toString().equals(forgetPwd2Edit.getText().toString()))){
                    ToastUtil.showShort(R.string.forget_pwd_check);
                    return;
                }
                ModifyPwd(forgetNameEdit.getText().toString(),forgetPwd2Edit.getText().toString(), forgetCodeEdit.getText().toString());
                break;
        }
    }

    /**
     * 检查用户名是否可用
     * @param phone
     */
    private void getCode(String phone) {
        String url = HttpPath.getPath(HttpPath.FORGETPWD_GETCODE);
        RequestParams params = HttpUtil.params(url);
        params.addBodyParameter("mobile", phone);
        HttpUtil.http().post(params, new RequestCallBack<String>(activity){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);
                Type type = new TypeToken<ResultEntity>(){}.getType();
                ResultEntity entity = GsonUtil.GsonToBean(result, type);
                if(entity.isSuccee()){
                    ToastUtil.showShort("验证码以发送");
                    forgetGetcodeTxt.setClickable(false);
                    downTimer = new CountDownTimer(60 * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            forgetGetcodeTxt.setText(String.format(getResources().getString(R.string.code_wait), (millisUntilFinished / 1000) + ""));
                        }

                        @Override
                        public void onFinish() {
                            forgetGetcodeTxt.setClickable(true);
                            forgetGetcodeTxt.setText(R.string.code_get);
                        }
                    };
                    downTimer.start();
                }else{
                    ToastUtil.showShort("验证码获取失败，请检查手机号码");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
//                ToastUtil.showShort(ex.getMessage());
            }
        });

    }

    /**
     * 验证验证码
     * @param phone
     *//*
    private void checkCode(String phone, String code) {
        String url = HttpPath.getPath(HttpPath.FORGETPWD_CHECKCODE);
        RequestParams params = HttpUtil.params(url);
        params.addBodyParameter("mobile", phone);
        params.addBodyParameter("code", code);
        HttpUtil.http().post(params, new RequestCallBack<String>(activity){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                DialogUtils.stopProgress(activity);
                LogUtil.d(result);
                Type type = new TypeToken<ResultEntity>(){}.getType();
                ResultEntity entity = GsonUtil.GsonToBean(result, type);
                if(entity.isSuccee()){
                    ToastUtil.showShort("验证码验证成功，开始修改密码");
                    //修改密码
                }else{
                    ToastUtil.showShort("验证码验证失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                DialogUtils.stopProgress(activity);
                ToastUtil.showShort(ex.getMessage());
            }
        });

    }*/

    /**
     * 验证验证码
     * @param phone
     */
    private void ModifyPwd(String phone, final String pwd, String code) {
        String url = HttpPath.getPath(HttpPath.FORGETPWD);
        RequestParams params = HttpUtil.params(url);
        params.addBodyParameter("mobile", phone);
        params.addBodyParameter("code", code);
        params.addBodyParameter("password", pwd);
        HttpUtil.http().post(params, new RequestCallBack<String>(activity){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                DialogUtils.stopProgress(activity);
                LogUtil.d(result);
                Type type = new TypeToken<ResultEntity>(){}.getType();
                ResultEntity entity = GsonUtil.GsonToBean(result, type);
                if(entity.isSuccee()){
                    ToastUtil.showShort("修改成功");
                    SharedData.saveUserPwd(pwd);
                    activity.finish();
                    //修改密码
                }else{
                    ToastUtil.showShort("修改失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                DialogUtils.stopProgress(activity);
//                ToastUtil.showShort(ex.getMessage());
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (downTimer != null) {
            downTimer.cancel();
        }
    }
}
