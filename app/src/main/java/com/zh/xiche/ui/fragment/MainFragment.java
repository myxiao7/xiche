package com.zh.xiche.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zh.xiche.R;
import com.zh.xiche.base.BaseFragment;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.config.SharedData;
import com.zh.xiche.entity.ResultEntity;
import com.zh.xiche.entity.UserInfoEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.ui.OrderListActivity;
import com.zh.xiche.ui.myorder.MyOrderSwitch;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.GsonUtil;
import com.zh.xiche.utils.ToastUtil;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页
 * Created by win7 on 2016/9/21.
 */

public class MainFragment extends BaseFragment {
    @Bind(R.id.bmapView)
    MapView bmapView;
    @Bind(R.id.frag_main_list_tv)
    TextView fragMainListTv;
    @Bind(R.id.frag_main_list2_tv)
    TextView fragMainList2Tv;
    private View mView;

//    MapView mMapView = null;
    SupportMapFragment mMapView;
    BaiduMap mBaiduMap;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    private UserInfoEntity entity;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("", 1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_main, container, false);
        }
        ButterKnife.bind(this, mView);
        entity = DbUtils.getInstance().getPersonInfo();
        mMapView = SupportMapFragment.newInstance();
        // 地图初始化
        mBaiduMap = bmapView.getMap();
        //卫星地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //初始化地图显示区域
        if(!TextUtils.isEmpty(SharedData.getCurrentlat()) && !TextUtils.isEmpty(SharedData.getCurrentlon())){
            LogUtil.d(SharedData.getCurrentlat());
            LogUtil.d(SharedData.getCurrentlon());
            LogUtil.d(SharedData.getCurrentAdd());
            LatLng ll = new LatLng(Double.parseDouble(SharedData.getCurrentlat()),
                    Double.parseDouble(SharedData.getCurrentlon()));
            LogUtil.d(Double.parseDouble(SharedData.getCurrentlat()) + "," + Double.parseDouble(SharedData.getCurrentlon()));
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
        //6.0以上动态获取需求权限
        AndPermission.with(this)
                .requestCode(101)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .rationale(mRationaleListener)
                .send();

        return mView;
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

    /**
     * 初始化定位
     */
    private void initLocaticon() {

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(activity.getApplicationContext());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(2 * 60 * 1000);
        option.setAddrType("all");
        mLocClient.setLocOption(option);
        mLocClient.start();
        /*mCurrentMode = LocationMode.FOLLOWING;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, mCurrentMarker));*/

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            LogUtil.d(location.getAddrStr());
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(15.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            //更新位置
            updateLocation(location.getLongitude()+"", location.getLatitude()+"", location.getAddrStr());

            //保存位置坐标
            SharedData.saveCurrentlat(location.getLatitude()+"");
            SharedData.saveCurrentlon(location.getLongitude()+"");
            SharedData.saveCurrentAdd(location.getAddrStr()+"");
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    /**
     * 更新位置
     */
    private void updateLocation(String lon, String lat, String location){
        String path = HttpPath.getPath(HttpPath.MODIFYLOCATION);
        RequestParams params = HttpUtil.params(path);
        params.addBodyParameter("uid", entity.getId());
        params.addBodyParameter("tockens", entity.getTockens());
        params.addBodyParameter("location", location);
        params.addBodyParameter("lon", lon);
        params.addBodyParameter("lat", lat);
        HttpUtil.http().post(params, new RequestCallBack<String>(activity){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Type type = new TypeToken<ResultEntity>(){}.getType();
                ResultEntity resultEntity = GsonUtil.GsonToBean(result, type);
                if(resultEntity.isSuccee()){
//                    ToastUtil.showShort("更新位置成功");
                }else{
                    ToastUtil.showShort("更新位置失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                /*super.onError(ex, isOnCallback);
                ToastUtil.showShort(ex.getMessage());*/
            }
        });
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
//        ToastUtil.showShort("获取定位权限成功");
        initLocaticon();
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(101)
    private void getLocationNo() {
        // 申请权限失败，可以提醒一下用户。
        ToastUtil.showShort("定位失败，无法绘制路线");
    }


    @OnClick({R.id.frag_main_list_tv, R.id.frag_main_list2_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frag_main_list_tv:
                if(DbUtils.getInstance().getPersonInfo().getIspass() == 1){
                    Intent intent = new Intent(activity, MyOrderSwitch.class);
                    startActivity(intent);
                }else{
                    ToastUtil.showShort("请等待审核");
                }
                break;
            case R.id.frag_main_list2_tv:
                if(DbUtils.getInstance().getPersonInfo().getIspass() == 1){
                    Intent intent2 = new Intent(activity, OrderListActivity.class);
                    startActivity(intent2);
                }else{
                    ToastUtil.showShort("请等待审核");
                }
                break;
        }
    }

    @Override
    public void onPause() {
//        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
//        mMapView.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
      /*  mMapView.onDestroy();
        mMapView = null;*/
    }
}
