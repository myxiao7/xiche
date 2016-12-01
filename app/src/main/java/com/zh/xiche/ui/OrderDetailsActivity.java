package com.zh.xiche.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
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
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zh.xiche.R;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.base.BaseApplication;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.config.SharedData;
import com.zh.xiche.entity.OrderEntity;
import com.zh.xiche.entity.ResultEntity;
import com.zh.xiche.entity.UserInfoEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.DialogUtil;
import com.zh.xiche.utils.DrivingRouteOverlay;
import com.zh.xiche.utils.GsonUtil;
import com.zh.xiche.utils.ImageLoaderHelper;
import com.zh.xiche.utils.OverlayManager;
import com.zh.xiche.utils.ToastUtil;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.lang.reflect.Type;

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
    @Bind(R.id.getorder_nav_btn)
    FloatingActionButton getorderNavBtn;

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
    //导航起点和重点
    private LatLng mStartPoint, mEndPoint;
    //导航起点名称
    private String mStartLoc;

    private UserInfoEntity userInfoEntity; //个人信息
    private OrderEntity orderEntity; //订单详情
    private Double lon, lat;
    private int orderType = 1;// 订单类型（抢单，完成，已完成）
    private int position = 0;// 订单类型（抢单，完成，已完成）

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getorder);
        ButterKnife.bind(this);
        init();
        setOrderData();
        //初始化地图显示区域
        if (!TextUtils.isEmpty(SharedData.getCurrentlat()) && !TextUtils.isEmpty(SharedData.getCurrentlon())) {
            LatLng ll = new LatLng(Double.parseDouble(SharedData.getCurrentlat()),
                    Double.parseDouble(SharedData.getCurrentlon()));
            LogUtil.d(Double.parseDouble(SharedData.getCurrentlat()) + "," + Double.parseDouble(SharedData.getCurrentlon()));
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
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

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(60 * 60 * 1000);
        option.setAddrType("all");
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
        // 地图初始化
        mBaiduMap = mMapView.getMap();
        //卫星地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        userInfoEntity = DbUtils.getInstance().getPersonInfo();
    }

    /**
     * 设置订单信息
     */
    private void setOrderData() {
        Intent intent = this.getIntent();
        orderEntity = intent.getParcelableExtra("order");
        orderType = intent.getIntExtra("type", 1);
        position = intent.getIntExtra("position", 0);
        switch (orderType) {
            case 1:
                toolbarTv.setText("接单");
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
        if (!TextUtils.isEmpty(orderEntity.getAvartar())) {
            ImageLoaderHelper.getInstance().loadCirPic(getorderIconImg, orderEntity.getAvartar());
        }
        //昵称 + 姓名
        getorderNameTv.setText(orderEntity.getName());
        if (!TextUtils.isEmpty(orderEntity.getMobile())) {
            //手机号码
            getorderPhoneTv.setText(orderEntity.getMobile());
            getorderPhoneImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("tel:" + orderEntity.getMobile());
                    activity.startActivity(new Intent(Intent.ACTION_DIAL, uri));
                }
            });
        }

        if (!TextUtils.isEmpty(orderEntity.getOrderid())) {
            //订单号码
            getorderOrderTv.setText(orderEntity.getOrderid());
        }
        if (!TextUtils.isEmpty(orderEntity.getServicetypename())) {
            //洗车类型
            getorderTypeTv.setText(orderEntity.getServicetypename());
        }
        if (!TextUtils.isEmpty(orderEntity.getLocation())) {
            //地址
            getorderAddTv.setText(orderEntity.getLocation());
        }
        if (!TextUtils.isEmpty(orderEntity.getAppointment())) {
            //预约时间
            getorderTimeTv.setText(orderEntity.getAppointment());
        }
        if (!TextUtils.isEmpty(orderEntity.getCarbrank())) {
            //车型
            getorderCartypeTv.setText(orderEntity.getCarbrank());
        }
        if (!TextUtils.isEmpty(orderEntity.getCarcolor())) {
            //颜色
            getorderColorTv.setText(orderEntity.getCarcolor());
        }
        if (!TextUtils.isEmpty(orderEntity.getCarno())) {
            //车牌
            getorderNumTv.setText(orderEntity.getCarno());
        }
        if (!TextUtils.isEmpty(orderEntity.getOrderamount() + "")) {
            //价格
            getorderPriceTv.setText("￥" + orderEntity.getOrderamount() + "");
        }
        if (!TextUtils.isEmpty(orderEntity.getOrderid())) {
            //订单号码
            getorderOrderTv.setText("订单号:" + orderEntity.getOrderid());
        }
        if (!TextUtils.isEmpty(orderEntity.getRemark())) {
            //备注
            getorderRemarkTv.setText(orderEntity.getRemark());
        }
        lon = orderEntity.getLon();
        lat = orderEntity.getLat();
    }


    @OnClick(R.id.getorder_get_btn)
    public void onClick() {
        switch (orderType) {
            case 1:
                ToastUtil.showShort("接单");
                getOrder();
                break;
            case 2:
                ToastUtil.showShort("完成");
                finishOrder();
                break;
            case 3:
                ToastUtil.showShort("已完成");
                break;

        }
    }

    @OnClick(R.id.getorder_nav_btn)
    public void onClick2() {
        startNavi();
    }
    /**
     * 启动百度地图导航(Native)
     */
    public void startNavi() {
        LatLng pt1 = mStartPoint;
        LatLng pt2 = mEndPoint;

        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2)
                .startName(mStartLoc).endName(orderEntity.getLocation());
        try {
            BaiduMapNavigation.openBaiduMapNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            ToastUtil.showShort("您尚未安装百度地图app或app版本过低,为您打开Web导航");
            startWebNavi();
        }

    }

    /**
     * 启动百度地图导航(Web)
     */
    public void startWebNavi() {
        LatLng pt1 = mStartPoint;
        LatLng pt2 = mEndPoint;
        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(pt1).endPoint(pt2);

        BaiduMapNavigation.openWebBaiduMapNavi(para, this);
    }


    /**
     * 接单
     */
    private void getOrder() {
        String path = HttpPath.getPath(HttpPath.ORDERACCEPT);
        RequestParams params = HttpUtil.params(path);
        params.addBodyParameter("uid", userInfoEntity.getId());
        params.addBodyParameter("tockens", userInfoEntity.getTockens());
        params.addBodyParameter("orderid", orderEntity.getOrderid());
        HttpUtil.http().post(params, new RequestCallBack<String>(activity) {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Type type = new TypeToken<ResultEntity>() {
                }.getType();
                ResultEntity resultEntity = GsonUtil.GsonToBean(result, type);
                if (resultEntity.isSuccee(activity)) {
                    ToastUtil.showShort("接单成功，正在生成路线...");
                    orderType = 2;
                    getorderGetBtn.setText("完 成");
                    //绘制路线
                    DialogUtil.showProgress(activity);
                    mSearch.drivingSearch((new DrivingRoutePlanOption())
                            .from(stNode)
                            .to(enNode));
                } else {
                    ToastUtil.showShort(resultEntity.getError_desc());
                    activity.finish();
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
     * 完成订单
     */
    private void finishOrder() {
        String path = HttpPath.getPath(HttpPath.ORDERFINISH);
        RequestParams params = HttpUtil.params(path);
        params.addBodyParameter("uid", userInfoEntity.getId());
        params.addBodyParameter("tockens", userInfoEntity.getTockens());
        params.addBodyParameter("orderid", orderEntity.getOrderid());
        HttpUtil.http().post(params, new RequestCallBack<String>(activity) {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Type type = new TypeToken<ResultEntity>() {
                }.getType();
                ResultEntity resultEntity = GsonUtil.GsonToBean(result, type);
                if (resultEntity.isSuccee(activity)) {
//                    ToastUtil.showShort("完成订单");
                    getorderGetBtn.setText("已完成");
                    getorderGetBtn.setClickable(false);
                    getorderGetBtn.setBackgroundResource(R.drawable.border_gray);
                    orderType = 3;
                    //更新我的订单列表
                    Intent intent = new Intent(BaseApplication.ORDERFINISH);
                    intent.putExtra("position", position);
                    sendBroadcast(intent);
                    LogUtil.d("更新订单状态。。。。。。。。。。");
                } else {
                    ToastUtil.showShort(resultEntity.getError_desc());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
//                ToastUtil.showShort(ex.getMessage());
            }
        });
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
            getorderNavBtn.setVisibility(View.VISIBLE);
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
            mStartLoc = location.getAddrStr();
            //定位起点 导航起点
            mStartPoint = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(mStartPoint).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            //设置起点和终点
            stNode = PlanNode.withLocation(mStartPoint);
            // 导航终点
            mEndPoint = new LatLng(lat, lon);
            enNode = PlanNode.withLocation(mEndPoint);
            if (orderType != 1) {
                DialogUtil.showProgress(activity);
//                getorderNavBtn.setVisibility(View.VISIBLE);
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
//        ToastUtil.showShort("获取定位权限成功");
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
        BaiduMapNavigation.finish(this);
        super.onDestroy();
    }

}