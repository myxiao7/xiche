package com.zh.xiche.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.reflect.TypeToken;
import com.zh.xiche.R;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.config.FilePath;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.config.SharedData;
import com.zh.xiche.entity.ResultEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.utils.CommonCheck;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.DialogUtil;
import com.zh.xiche.utils.DialogUtils;
import com.zh.xiche.utils.GsonUtil;
import com.zh.xiche.utils.ToastUtil;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by win7 on 2016/9/18.
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.login_name_txt)
    EditText loginNameTxt;
    @Bind(R.id.login_pwd_txt)
    EditText loginPwdTxt;
    @Bind(R.id.login_forget_txt)
    TextView loginForgetTxt;
    @Bind(R.id.login_login_btn)
    Button loginLoginBtn;
    @Bind(R.id.login_register_txt)
    TextView loginRegisterTxt;

    private static final int REGISTERCODE = 0x1001;

    private MaterialDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if(!TextUtils.isEmpty(SharedData.getUserName()) && !TextUtils.isEmpty(SharedData.getUserPwd())){
            loginNameTxt.setText(SharedData.getUserName());
            loginPwdTxt.setText(SharedData.getUserPwd());
        }
    }

    @OnClick({R.id.login_login_btn, R.id.login_register_txt, R.id.login_forget_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_login_btn:
                if(TextUtils.isEmpty(loginNameTxt.getText().toString())){
                    ToastUtil.showShort(R.string.login_phone_hint);
                    return;
                }
                if(!CommonCheck.isMobile(loginNameTxt.getText().toString())){
                    ToastUtil.showShort(R.string.login_vai_hint);
                    return;
                }

                if(TextUtils.isEmpty(loginPwdTxt.getText().toString())){
                    ToastUtil.showShort(R.string.login_pwd_hint);
                    return;
                }

                DialogUtils.showProgress(activity);
                login();
//                downloadFile("");
                break;
            case R.id.login_register_txt:
                Intent intent2 = new Intent(activity, RegisterActivity.class);
                startActivityForResult(intent2, REGISTERCODE);
                activity.finish();
                break;
            case R.id.login_forget_txt:
                Intent intent3 = new Intent(activity, ForgetActivity.class);
                startActivity(intent3);
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        String url = HttpPath.getPath(HttpPath.LOGIN);
        RequestParams params = HttpUtil.params(url);
        params.addBodyParameter("mobile", loginNameTxt.getText().toString());
        params.addBodyParameter("password", loginPwdTxt.getText().toString());
        HttpUtil.http().post(params, new RequestCallBack<String>(activity) {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                DialogUtils.stopProgress(activity);
                LogUtil.d(result);
                Type type = new TypeToken<ResultEntity>() {
                }.getType();
                ResultEntity entity = GsonUtil.GsonToBean(result, type);
                if (entity.isSuccee()) {
                    ToastUtil.showShort("登录成功");
                    //保存用户信息
                    DbUtils.getInstance().clearPersonInfo();
//                    entity.getOperatorDTO().setIspass(1);
                    DbUtils.getInstance().savePersonInfo(entity.getOperatorDTO());
                    SharedData.saveUserName(loginNameTxt.getText().toString());
                    SharedData.saveUserPwd(loginPwdTxt.getText().toString());
                    /*if(TextUtils.isEmpty(entity.getOperatorDTO().getCardno()) || TextUtils.isEmpty(entity.getOperatorDTO().getLocation()) || TextUtils.isEmpty(entity.getOperatorDTO().getName())){
                        ToastUtil.showShort("请先完善个人信息");
                        //去填写个人信息
                        Intent intent = new Intent(activity, ModifyUserInfoActivity.class);
                        intent.putExtra("isRegister", false);
                        startActivity(intent);
                    }else{
                        //去首页
                        Intent intent = new Intent(activity, MainActivity.class);
                        startActivity(intent);
                    }*/
                    JPushInterface.setAlias(activity, entity.getOperatorDTO().getId(), new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                        LogUtil.d("JPushInterface code" + i + "userid" + s.toString());

                        }
                    });
                    //去首页
                    Intent intent = new Intent(activity, MainActivity.class);
                    startActivity(intent);
                    activity.finish();
                } else {
                    ToastUtil.showShort("登录失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                DialogUtils.stopProgress(activity);
                ToastUtil.showShort("网络错误");
            }
        });

    }

    private void downloadFile(String url){
        dialog = new MaterialDialog.Builder(this)
                .title("更新")
                .content("正在下载")
                .progress(false, 0, true)
                .cancelable(false)
                .show();
        RequestParams requestParams = new RequestParams("http://192.168.1.104:8080/examples/1.apk");
        requestParams.setSaveFilePath(FilePath.CACHE_PATH + "xiche.apk");
        HttpUtil.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                dialog.setMaxProgress((int) total);
                dialog.setProgress((int) current);

            }

            @Override
            public void onSuccess(File result) {
                ToastUtil.showShort("下载成功");
                dialog.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                ToastUtil.showShort("下载失败");
                dialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTERCODE && resultCode == RESULT_OK && data != null) {
            loginNameTxt.setText(data.getStringExtra("userName"));
            loginPwdTxt.setText(data.getStringExtra("userPwd"));
        }
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
