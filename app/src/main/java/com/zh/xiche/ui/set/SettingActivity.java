package com.zh.xiche.ui.set;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.google.gson.reflect.TypeToken;
import com.zh.xiche.R;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.base.BaseApplication;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.entity.ResultEntity;
import com.zh.xiche.entity.UserInfoEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.ui.ForgetActivity;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.DialogUtils;
import com.zh.xiche.utils.GsonUtil;
import com.zh.xiche.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.lang.reflect.Type;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by win7 on 2016/11/8.
 */

public class SettingActivity extends BaseActivity {
    @Bind(R.id.toolbar_tv)
    TextView toolbarTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.set_logout_tv)
    TextView setLogoutTv;
    @Bind(R.id.set_forget_tv)
    TextView setForgetTv;
    @Bind(R.id.set_about_tv)
    TextView setAboutTv;
    private UserInfoEntity entity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
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
        toolbarTv.setText("设置");
        entity = DbUtils.getInstance().getPersonInfo();
    }
    @OnClick({R.id.set_logout_tv, R.id.set_forget_tv, R.id.set_about_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_logout_tv:
                DialogUtils.showProgress(activity);
                logout();
                break;
            case R.id.set_forget_tv:
                Intent intent = new Intent(activity, ForgetActivity.class);
                startActivity(intent);
                break;
            case R.id.set_about_tv:
                break;
        }
    }

    /**
     * 注销用户
     */
    private void logout() {
        String path = HttpPath.getPath(HttpPath.LOGINOUT);
        RequestParams params = HttpUtil.params(path);
        params.addBodyParameter("uid", entity.getId());
        params.addBodyParameter("tockens", entity.getTockens());
        HttpUtil.http().post(params, new RequestCallBack<String>(activity) {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Type type = new TypeToken<ResultEntity>() {
                }.getType();
                ResultEntity resultEntity = GsonUtil.GsonToBean(result, type);
                if (resultEntity.isSuccee()) {
                    JPushInterface.setAlias(activity, "", new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                            DialogUtils.stopProgress(activity);
                            ToastUtil.showShort("注销成功");
                            //发送注销广播
                            Intent intent1 = new Intent(BaseApplication.LOGOUT);
                            activity.sendBroadcast(intent1);
                            activity.finish();
                        }
                    });
                } else {
                    DialogUtils.stopProgress(activity);
                    ToastUtil.showShort("注销失败");
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
}
