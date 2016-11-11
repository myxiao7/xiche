package com.zh.xiche.ui.receive;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.google.gson.reflect.TypeToken;
import com.zh.xiche.R;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.entity.OrderDetailsEntity;
import com.zh.xiche.entity.OrderEntity;
import com.zh.xiche.entity.PushEntity;
import com.zh.xiche.entity.ResultEntity;
import com.zh.xiche.entity.UserInfoEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.ui.OrderDetailsActivity;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.DialogUtil;
import com.zh.xiche.utils.GsonUtil;
import com.zh.xiche.utils.ToastUtil;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 派单
 * Created by zhanghao on 2016/10/13.
 */

public class GiveOrderActivity extends Activity {
    @Bind(R.id.giveorder_icon_img)
    ImageView giveorderIconImg;
    @Bind(R.id.giveorder_name_tv)
    TextView giveorderNameTv;
    @Bind(R.id.giveorder_phone_tv)
    TextView giveorderPhoneTv;
    @Bind(R.id.giveorder_order_tv)
    TextView giveorderOrderTv;
    @Bind(R.id.giveorder_phone_img)
    ImageView giveorderPhoneImg;
    @Bind(R.id.giveorder_type_tv)
    TextView giveorderTypeTv;
    @Bind(R.id.giveorder_add_tv)
    TextView giveorderAddTv;
    @Bind(R.id.giveorder_time_tv)
    TextView giveorderTimeTv;
    @Bind(R.id.giveorder_price_tv)
    TextView giveorderPriceTv;
    @Bind(R.id.giveorder_cartype_tv)
    TextView giveorderCartypeTv;
    @Bind(R.id.giveorder_color_tv)
    TextView giveorderColorTv;
    @Bind(R.id.giveorder_num_tv)
    TextView giveorderNumTv;
    @Bind(R.id.giveorder_remark_tv)
    TextView giveorderRemarkTv;
    @Bind(R.id.giveorder_accept_btn)
    Button giveorderAcceptBtn;
    @Bind(R.id.giveorder_refuse_btn)
    Button giveorderRefuseBtn;
    @Bind(R.id.giveorder_timedown_tv)
    TextView giveorderTimedownTv;

    private CountDownTimer downTimer;
    private UserInfoEntity userInfoEntity;

    private PushEntity pushEntity;


    long waitTime = 2000;
    long touchTime = 0;
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if((currentTime-touchTime)>=waitTime) {
            ToastUtil.showShort("再按一次退出");
            touchTime = currentTime;
        }else {
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giveorder);
        ButterKnife.bind(this);
        userInfoEntity = DbUtils.getInstance().getPersonInfo();
        pushEntity = this.getIntent().getParcelableExtra("order");
        LogUtil.d("初始。。。。。" + pushEntity.getOrder_id());
        giveorderTypeTv.setText(pushEntity.getService_type());
        giveorderAddTv.setText(pushEntity.getOrder_Location());
        giveorderTimeTv.setText(pushEntity.getAppointment());
        giveorderCartypeTv.setText(pushEntity.getCar_style());
        giveorderRemarkTv.setText(pushEntity.getRemark());

        downTimer = new CountDownTimer(60 *1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                giveorderTimedownTv.setText("" + millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
//                refuseOrder();
                finish();
            }
        }.start();
    }

    @OnClick({R.id.giveorder_accept_btn, R.id.giveorder_refuse_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.giveorder_accept_btn:
                DialogUtil.showProgress(this);
                getOrder();
                break;
            case R.id.giveorder_refuse_btn:
                refuseOrder();
                finish();
                break;
        }
    }

    /**
     * 接单
     */
    private void getOrder() {
        String path = HttpPath.getPath(HttpPath.ORDERACCEPT);
        RequestParams params = HttpUtil.params(path);
        params.addBodyParameter("uid", userInfoEntity.getId());
        params.addBodyParameter("tockens", userInfoEntity.getTockens());
        params.addBodyParameter("orderid", pushEntity.getOrder_id());
        LogUtil.d("接单号码" + pushEntity.getOrder_id());
        HttpUtil.http().post(params, new RequestCallBack<String>(GiveOrderActivity.this) {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Type type = new TypeToken<ResultEntity>() {
                }.getType();
                ResultEntity resultEntity = GsonUtil.GsonToBean(result, type);
                if (resultEntity.isSuccee()) {
                    ToastUtil.showShort("接单成功");
                    getOrderDetails();
                } else {
                    ToastUtil.showShort("接单失败...");
                }
                GiveOrderActivity.this.finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
//                ToastUtil.showShort(ex.getMessage());
                DialogUtil.stopProgress(GiveOrderActivity.this);
            }
        });
    }

    /**
     * 订单详情
     * @param
     */
    private void getOrderDetails() {
        String path = HttpPath.getPath(HttpPath.ORDERDETAILS);
        RequestParams params = HttpUtil.params(path);
        params.addBodyParameter("uid", userInfoEntity.getId());
        params.addBodyParameter("tockens", userInfoEntity.getTockens());
        params.addBodyParameter("orderid", pushEntity.getOrder_id());
        HttpUtil.http().post(params, new RequestCallBack<String>(GiveOrderActivity.this) {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Type type = new TypeToken<OrderDetailsEntity>() {
                }.getType();
                OrderDetailsEntity detailsEntity = GsonUtil.GsonToBean(result, type);
                if (detailsEntity.isSuccee()) {
                    //获取订单详情
                    Intent intent = new Intent(GiveOrderActivity.this, OrderDetailsActivity.class);
                    intent.putExtra("order", detailsEntity.getOrdersDTO());
                    intent.putExtra("type", 2);
                    startActivity(intent);
                } else {
                    ToastUtil.showShort("获取订单详情失败,请查看待服务订单");
                }
                DialogUtil.stopProgress(GiveOrderActivity.this);
                GiveOrderActivity.this.finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
//                ToastUtil.showShort(ex.getMessage());
                DialogUtil.stopProgress(GiveOrderActivity.this);
                ToastUtil.showShort("获取订单详情失败,请查看待服务订单");
                GiveOrderActivity.this.finish();
            }
        });
    }
    /**
     * 据单
     */
    private void refuseOrder() {
        String path = HttpPath.getPath(HttpPath.ORDERREFUSER);
        RequestParams params = HttpUtil.params(path);
        params.addBodyParameter("uid", userInfoEntity.getId());
        params.addBodyParameter("tockens", userInfoEntity.getTockens());
        params.addBodyParameter("orderid", pushEntity.getOrder_id());
        HttpUtil.http().post(params, new RequestCallBack<String>(GiveOrderActivity.this) {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Type type = new TypeToken<ResultEntity>() {
                }.getType();
                ResultEntity resultEntity = GsonUtil.GsonToBean(result, type);
                if (resultEntity.isSuccee()) {
                    ToastUtil.showShort("拒单成功");
                    GiveOrderActivity.this.finish();
                } else {
                    ToastUtil.showShort("拒单失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
//                ToastUtil.showShort(ex.getMessage());
            }
        });
    }
//    int i = 0;
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        finish();
        LogUtil.d("finish");
        //保证一个实例，且后一个替换掉前一个
        /*Intent intent1 = new Intent(this, GiveOrderActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra("order", intent.getParcelableExtra("order"));
        startActivity(intent1);*/
        userInfoEntity = DbUtils.getInstance().getPersonInfo();
        pushEntity = intent.getParcelableExtra("order");
        LogUtil.d("订单号" + pushEntity.getOrder_id());
        giveorderTypeTv.setText(pushEntity.getService_type());
        giveorderAddTv.setText(pushEntity.getOrder_Location());
        giveorderTimeTv.setText(pushEntity.getAppointment());
        giveorderCartypeTv.setText(pushEntity.getCar_style());
        giveorderRemarkTv.setText(pushEntity.getRemark());
        downTimer.cancel();
        downTimer = new CountDownTimer(60 *1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                giveorderTimedownTv.setText("" + millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
//                refuseOrder();
                finish();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        downTimer.cancel();
        ((NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
    }
}
