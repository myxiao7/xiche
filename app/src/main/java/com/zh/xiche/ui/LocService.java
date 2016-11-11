package com.zh.xiche.ui;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.config.SharedData;
import com.zh.xiche.entity.ResultEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.ui.fragment.MainFragment;
import com.zh.xiche.utils.GsonUtil;
import com.zh.xiche.utils.ToastUtil;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.lang.reflect.Type;

import butterknife.ButterKnife;

/**
 * Created by win7 on 2016/11/11.
 */

public class LocService extends Service {
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLocaticon();
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
        option.setScanSpan(1 * 60 * 1000);
        option.setAddrType("all");
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
            LogUtil.d(location.getAddrStr());
            //更新位置
//            updateLocation(location.getLongitude()+"", location.getLatitude()+"", location.getAddrStr());
            //保存位置坐标
            SharedData.saveCurrentlat(location.getLatitude()+"");
            SharedData.saveCurrentlon(location.getLongitude()+"");
            SharedData.saveCurrentAdd(location.getAddrStr()+"");
            ToastUtil.showShort("保存地址" + location.getLatitude()+"" + location.getAddrStr()+"");
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        mLocClient.stop();
    }
}

