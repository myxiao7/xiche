package com.zh.xiche.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.google.gson.reflect.TypeToken;
import com.zh.xiche.R;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.entity.ResultEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.utils.DialogUtil;
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

    private static final int CITYREQUEST = 0x1001;
    private String userName, userPwd;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    private Double lon ,lar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_userinfo);
        init();
        int checkPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ToastUtil.showShort("未打开位置开关");
            return;
        } else {
            initLocaticon();
        }
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
        toolbarTv.setText("完善个人信息");
        Intent intent = this.getIntent();
        userName = intent.getStringExtra("userName");
        userPwd = intent.getStringExtra("userPwd");
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
                Intent intent2 = new Intent(activity, RegisterResultActivity.class);
                startActivity(intent2);
                break;
        }
    }

    /**
     * 完善用户信息
     */
    private void modifyUserInfo() {
        String url = HttpPath.getPath(HttpPath.REGISTER);
        RequestParams params = HttpUtil.params(url);
        params.addBodyParameter("uid", registerNameEdit.getText().toString());
        params.addBodyParameter("tockens", "");
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
                    ToastUtil.showShort("注册成功");
                    //去结果页面
                    Intent intent = new Intent(activity, RegisterResultActivity.class);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userPwd", userPwd);
                    startActivity(intent);
                }else{
                    ToastUtil.showShort("注册失败");
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
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
