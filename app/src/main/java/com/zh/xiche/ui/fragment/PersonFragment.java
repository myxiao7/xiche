package com.zh.xiche.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.reflect.TypeToken;
import com.zh.xiche.R;
import com.zh.xiche.adapter.BillListAdapetr;
import com.zh.xiche.base.BaseFragment;
import com.zh.xiche.config.FilePath;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.entity.Amont;
import com.zh.xiche.entity.BillYearEntity;
import com.zh.xiche.entity.JsonModel;
import com.zh.xiche.entity.UserInfoEntity;

import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.ui.mybill.BillByAllActivity;
import com.zh.xiche.ui.PersonInfo;
import com.zh.xiche.ui.myorder.MyOrderSwitch;
import com.zh.xiche.ui.set.SettingActivity;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.GsonUtil;
import com.zh.xiche.utils.ImageLoaderHelper;
import com.zh.xiche.utils.ListViewUtils;
import com.zh.xiche.utils.ToastUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人中心
 * Created by win7 on 2016/9/21.
 */

public class PersonFragment extends BaseFragment {

    @Bind(R.id.per_username_tv)
    TextView perUsernameTv;
    @Bind(R.id.per_account_tv)
    TextView perAccountTv;
    @Bind(R.id.per_account_re)
    RelativeLayout perAccountRe;
    @Bind(R.id.per_ordercount_tv)
    TextView perOrdercountTv;
    @Bind(R.id.per_moneycount_tv)
    TextView perMoneycountTv;
    @Bind(R.id.per_mydrder_tv)
    TextView perMydrderTv;
    @Bind(R.id.per_mybill_tv)
    TextView perMybillTv;
    @Bind(R.id.per_setting_tv)
    View perSettingTv;
    @Bind(R.id.per_icon_img)
    ImageView perIconImg;
    private View mView;

    private UserInfoEntity entity;
    private MaterialDialog dialog;

    public static PersonFragment newInstance() {
        PersonFragment fragment = new PersonFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("", 1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_person, container, false);
        }
        ButterKnife.bind(this, mView);
        entity = DbUtils.getInstance().getPersonInfo();
        perUsernameTv.setText(entity.getMobile());

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(DbUtils.getInstance().getPersonInfo().getAvatar())){
            ImageLoaderHelper.getInstance().loadCirPic(perIconImg, HttpPath.HOST + DbUtils.getInstance().getPersonInfo().getAvatar());
        }
        getCountByDay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.per_account_re, R.id.per_mydrder_tv, R.id.per_mybill_tv, R.id.per_setting_tv})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.per_account_re:
                ToastUtil.showShort(R.string.per_account);
                intent = new Intent(activity, PersonInfo.class);
                startActivity(intent);
                break;
            case R.id.per_mydrder_tv:
                if(DbUtils.getInstance().getPersonInfo().getIspass() == 1){
                    intent = new Intent(activity, MyOrderSwitch.class);
                    startActivity(intent);
                }else{
                    ToastUtil.showShort("请等待审核");
                }
                break;
            case R.id.per_mybill_tv:
                if(DbUtils.getInstance().getPersonInfo().getIspass() == 1){
                    intent = new Intent(activity, BillByAllActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtil.showShort("请等待审核");
                }
                break;
            case R.id.per_setting_tv:
                intent = new Intent(activity, SettingActivity.class);
                startActivity(intent);
//                ToastUtil.showShort(R.string.per_setting);
//                downloadFile("");
                break;
        }
    }

    private void downloadFile(String url){
        dialog = new MaterialDialog.Builder(activity)
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

    /**
     * 获取订单数量和金额
     */
    private void getCountByDay(){
        String path = HttpPath.getPath(HttpPath.GETCOUNTBYDAY);
        RequestParams params = HttpUtil.params(path);
        params.addBodyParameter("uid", entity.getId());
        params.addBodyParameter("tockens", entity.getTockens());
        HttpUtil.http().post(params, new RequestCallBack<String>(activity){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Type type = new TypeToken<Amont>(){}.getType();
                Amont amont  = GsonUtil.GsonToBean(result, type);
                if(amont.isSuccess()){
                    perOrdercountTv.setText(amont.getOperStatisticsDTO().getNum());
                    perMoneycountTv.setText(amont.getOperStatisticsDTO().getAmmount());
                }else{
                    ToastUtil.showShort("请求失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                ToastUtil.showShort(ex.getMessage());
            }
        });
    }
}
