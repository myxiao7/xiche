package com.zh.xiche.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.zh.xiche.R;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.entity.BaseEditEnum;
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

/**
 * 通用编辑
 * Created by zhanghao on 2016/10/10.
 */

public class CommonEdit extends BaseActivity {

    public static final String REQTYPE = "REQTYPE";//每集类型key
    public static final String BASESTR = "BASESTR";//原字符串key
    public static final String RESULTSTR = "RESULTSTR";//修改后字符串key
    @Bind(R.id.toolbar_tv)
    TextView toolbarTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.commonedit_edit)
    EditText commoneditEdit;

    private UserInfoEntity entity;
    private BaseEditEnum baseEditEnum;//要修改的枚举
    private int reqId = 1;//id
    private String str = "";//原内容

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commonedit);
        init();
        Intent intent = this.getIntent();
        reqId = intent.getIntExtra(REQTYPE, 1);
        str = intent.getStringExtra(BASESTR);
        baseEditEnum = BaseEditEnum.getTypebyId(reqId);
        toolbarTv.setText(baseEditEnum.getTitle());
        commoneditEdit.setText(str);
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
        entity = DbUtils.getInstance().getPersonInfo();
    }

    /**
     * 修改用户信息
     */
    private void modifyUserInfo() {
        String url = HttpPath.getPath(HttpPath.MODIFYINFO);
        RequestParams params = HttpUtil.params(url);
        params.addBodyParameter("uid", entity.getId());
        params.addBodyParameter("tockens", entity.getTockens());
        params.addBodyParameter(baseEditEnum.getParamName(), commoneditEdit.getText().toString());
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
                    Intent intent = new Intent();
                    intent.putExtra(REQTYPE,reqId);
                    intent.putExtra(RESULTSTR,commoneditEdit.getText().toString());
                    setResult(RESULT_OK, intent);
                    //更新数据库
                    DbUtils.getInstance().updateUserInfo(reqId,commoneditEdit.getText().toString());
                    activity.finish();
                }else{
                    ToastUtil.showShort("提交失败");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, Menu.FIRST + 1, 0, R.string.submit).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case Menu.FIRST + 1:
                if(commoneditEdit.getText().toString().equals(str) || TextUtils.isEmpty(commoneditEdit.getText().toString())){
                    ToastUtil.showShort("未作任何改动");
                    hideSoftKeybord();
                    activity.finish();
                }else{
                    modifyUserInfo();
                }

                break;
        }
        return true;
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftKeybord() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
