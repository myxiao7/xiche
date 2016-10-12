package com.zh.xiche.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zh.xiche.R;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.entity.OrderEntity;
import com.zh.xiche.utils.DialogUtil;
import com.zh.xiche.utils.DrivingRouteOverlay;
import com.zh.xiche.utils.ImageLoaderHelper;
import com.zh.xiche.utils.OverlayManager;
import com.zh.xiche.utils.ToastUtil;

import org.xutils.common.util.LogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2016/10/11.
 */

public class OrderDetailsActivity extends BaseActivity implements OnGetRoutePlanResultListener {
    @Bind(R.id.toolbar_tv)
    TextView toolbarTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.getorder_name_tv)
    TextView getorderNameTv;
    @Bind(R.id.getorder_phone_tv)
    TextView getorderPhoneTv;
    @Bind(R.id.getorder_order_tv)
    TextView getorderOrderTv;
    @Bind(R.id.getorder_type_tv)
    TextView getorderTypeTv;
    @Bind(R.id.getorder_add_tv)
    TextView getorderAddTv;
    @Bind(R.id.getorder_time_tv)
    TextView getorderTimeTv;
    @Bind(R.id.getorder_price_tv)
    TextView getorderPriceTv;
    @Bind(R.id.getorder_cartype_tv)
    TextView getorderCartypeTv;
    @Bind(R.id.getorder_color_tv)
    TextView getorderColorTv;
    @Bind(R.id.getorder_get_btn)
    Button getorderGetBtn;
    @Bind(R.id.getorder_icon_img)
    ImageView getorderIconImg;
    @Bind(R.id.mapView)
    MapView mapView;
    @Bind(R.id.getorder_num_tv)
    TextView getorderNumTv;
    @Bind(R.id.getorder_remark_tv)
    TextView getorderRemarkTv;
    @Bind(R.id.getorder_phone_img)
    ImageView getorderPhoneImg;

    MapView mMapView;
    BaiduMap mBaiduMap;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();


    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;

    // 搜索相关
    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    PlanNode stNode, enNode;//起点, 终点

    private OrderEntity entity; //订单详情
    private Double lon, lat;
    private int type = 1;// 订单类型（抢单，完成，已完成）

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getorder);
        ButterKnife.bind(this);
        init();
        setOrderData();
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

    /**
     * 初始化定位
     */
    private void initLocaticon() {
        // 地图初始化
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(60 * 60 * 1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        /*mCurrentMode = LocationMode.FOLLOWING;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, mCurrentMarker));*/
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        LogUtil.d("定位123");

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
        mMapView = (MapView) findViewById(R.id.mapView);
    }

    /**
     * 设置订单信息
     */
    private void setOrderData() {
        Intent intent = this.getIntent();
        entity = intent.getParcelableExtra("order");
        type = intent.getIntExtra("type", 1);
        switch (type) {
            case 1:
                toolbarTv.setText("抢单");
                getorderGetBtn.setText("接 单");
                break;
            case 2:
                toolbarTv.setText("订单信息");
                getorderGetBtn.setText("完 成");
                break;
            case 3:
                toolbarTv.setText("订单信息");
                getorderGetBtn.setText("已完成");
                getorderGetBtn.setClickable(false);
                getorderGetBtn.setBackgroundResource(R.drawable.border_gray);
                break;

        }
        if (!TextUtils.isEmpty(entity.getAvartar())) {
            ImageLoaderHelper.getInstance().loadPic(getorderIconImg, entity.getAvartar());
        }
        //昵称 + 姓名
        getorderNameTv.setText(entity.getUname() + "(" + entity.getName() + ")");
        if (!TextUtils.isEmpty(entity.getMobile())) {
            //手机号码
            getorderPhoneTv.setText(entity.getMobile());
            getorderPhoneImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("tel:" + entity.getMobile());
                    activity.startActivity(new Intent(Intent.ACTION_DIAL, uri));
                }
            });
        }

        if (!TextUtils.isEmpty(entity.getOrderid())) {
            //订单号码
            getorderOrderTv.setText(entity.getOrderid());
        }
        if (!TextUtils.isEmpty(entity.getServicetypename())) {
            //洗车类型
            getorderTypeTv.setText(entity.getServicetypename());
        }
        if (!TextUtils.isEmpty(entity.getLocation())) {
            //地址
            getorderAddTv.setText(entity.getLocation());
        }
        if (!TextUtils.isEmpty(entity.getAppointment())) {
            //预约时间
            getorderTimeTv.setText(entity.getAppointment());
        }
        if (!TextUtils.isEmpty(entity.getCarbrank())) {
            //车型
            getorderCartypeTv.setText(entity.getCarbrank());
        }
        if (!TextUtils.isEmpty(entity.getCarcolor())) {
            //颜色
            getorderColorTv.setText(entity.getCarcolor());
        }
        if (!TextUtils.isEmpty(entity.getCarno())) {
            //车牌
            getorderNumTv.setText(entity.getCarno());
        }
        if (!TextUtils.isEmpty(entity.getOrderamount() + "")) {
            //价格
            getorderPriceTv.setText("￥" + entity.getOrderamount() + "");
        }
        if (!TextUtils.isEmpty(entity.getOrderid())) {
            //订单号码
            getorderOrderTv.setText("订单号:" + entity.getOrderid());
        }
        if (!TextUtils.isEmpty(entity.getRemark())) {
            //备注
            getorderRemarkTv.setText(entity.getRemark());
        }
        lon = entity.getLon();
        lat = entity.getLat();
    }


    @OnClick(R.id.getorder_get_btn)
    public void onClick() {
        switch (type) {
            case 1:
                ToastUtil.showShort("抢单");
                type = 2;
                getorderGetBtn.setText("完 成");
                //绘制路线
                DialogUtil.showProgress(activity);
                mSearch.drivingSearch((new DrivingRoutePlanOption())
                        .from(stNode)
                        .to(enNode));
                break;
            case 2:
                ToastUtil.showShort("完成");
                getorderGetBtn.setText("已完成");
                getorderGetBtn.setClickable(false);
                getorderGetBtn.setBackgroundResource(R.drawable.border_gray);
                type = 3;
                break;
            case 3:
                ToastUtil.showShort("已完成");
                break;

        }
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        DialogUtil.stopProgress(activity);
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            ToastUtil.showShort(R.string.route_failt);
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        //
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            route = result.getRouteLines().get(0);
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
            routeOverlay = overlay;
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }


    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_st);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_en);
        }
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

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            //设置起点和终点
            /*PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", "龙泽");
            PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", "西单");*/
            stNode = PlanNode.withLocation(ll);
//            PlanNode enNode = PlanNode.withLocation(new LatLng(36.08246, 120.417519));
            enNode = PlanNode.withLocation(new LatLng(lat, lon));
            if (type != 1) {
                DialogUtil.showProgress(activity);
                //绘制路线
                mSearch.drivingSearch((new DrivingRoutePlanOption())
                        .from(stNode)
                        .to(enNode));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
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
        ToastUtil.showShort("获取定位权限成功");
        initLocaticon();
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(101)
    private void getLocationNo() {
        // 申请权限失败，可以提醒一下用户。
        ToastUtil.showShort("定位失败，无法绘制路线");
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        mSearch.destroy();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

}