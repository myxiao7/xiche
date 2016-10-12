package com.zh.xiche.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
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
 * 填写用户信息
 * Created by win7 on 2016/9/27.
 */

public class ModifyUserInfoActivity extends BaseActivity {
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

    private static final int CITYREQUEST = 0x1001;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    private Double lon ,lar;
    private UserInfoEntity infoEntity;

    @Override
    public void onBackPressed() {
        ToastUtil.showShort("请完善个人信息");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_userinfo);
        init();
        AndPermission.with(this)
                .requestCode(101)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .rationale(mRationaleListener)
                .send();

    }

    private RationaleListener mRationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            new MaterialDialog.Builder(activity)
                    .title("申请定位")
                    .content("没有定位权限,需要申请")
                    .positiveText("申请")
                    .negativeText("取消").onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    rationale.resume();// 用户同意继续申请。
                }
            }).onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    rationale.cancel(); // 用户拒绝申请。
                }
            }).show();
        }
    };
    private void init() {
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        /*toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });*/
        toolbarTv.setText("完善个人信息");
        infoEntity = DbUtils.getInstance().getPersonInfo();
    }


    /**
     * 初始化定位
     */
    private void initLocaticon() {
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        option.setScanSpan(60*60*1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            lar = location.getLatitude();
            lon = location.getLongitude();
            registerCitvTv.setText(location.getAddrStr());

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @OnClick({R.id.register_citv_tv, R.id.register_registe_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_citv_tv:
                Intent intent = new Intent(activity, SelectCityActivy.class);
                startActivityForResult(intent, CITYREQUEST);
                break;

            case R.id.register_registe_btn:
                modifyUserInfo();
                break;
        }
    }

    /**
     * 完善用户信息
     */
    private void modifyUserInfo() {
        String url = HttpPath.getPath(HttpPath.MODIFYINFO);
        RequestParams params = HttpUtil.params(url);
        params.addBodyParameter("uid", infoEntity.getId());
        params.addBodyParameter("tockens", infoEntity.getTockens());
        params.addBodyParameter("name", registerNameEdit.getText().toString());
        params.addBodyParameter("location", registerCitvTv.getText().toString());
        params.addBodyParameter("lon", lon + "");
        params.addBodyParameter("lat", lar + "");
        params.addBodyParameter("cardno", registerCardEdit.getText().toString());
        HttpUtil.http().post(params, new RequestCallBack<String>(activity){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                DialogUtils.stopProgress(activity);
                LogUtil.d(result);
                Type type = new TypeToken<ResultEntity>(){}.getType();
                ResultEntity entity = GsonUtil.GsonToBean(result, type);
                if(entity.isSuccee()){
                    ToastUtil.showShort("提交成功");
                    //去首页
                    Intent intent = new Intent(activity, MainActivity.class);
                    //更新信息
                    infoEntity.setCardno(registerCardEdit.getText().toString());
                    infoEntity.setName(registerNameEdit.getText().toString());
                    infoEntity.setLocation(registerCitvTv.getText().toString());
                    infoEntity.setLat(lar+"");
                    infoEntity.setLon(lon+"");
                    DbUtils.getInstance().clearPersonInfo();
                    DbUtils.getInstance().savePersonInfo(infoEntity);
                    startActivity(intent);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CITYREQUEST && resultCode == RESULT_OK && data!= null ){
            String city = data.getStringExtra("city");
            registerCitvTv.setText(city);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 只需要调用这一句，剩下的AndPermission自动完成。
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(101)
    private void getLocationYes() {
        // 申请权限成功，可以去做点什么了。
        Toast.makeText(this, "获取定位权限成功", Toast.LENGTH_SHORT).show();
        initLocaticon();
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(101)
    private void getLocationNo() {
        // 申请权限失败，可以提醒一下用户。
        Toast.makeText(this, "获取定位权限失败,请您选择城市", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLocClient != null){
            // 退出时销毁定位
            mLocClient.stop();
        }
        ButterKnife.unbind(this);
    }
}
