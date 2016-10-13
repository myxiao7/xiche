package com.zh.xiche.ui.receive;

import android.app.Activity;
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
import com.zh.xiche.entity.OrderEntity;
import com.zh.xiche.entity.ResultEntity;
import com.zh.xiche.entity.UserInfoEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.ui.OrderDetailsActivity;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.DialogUtil;
import com.zh.xiche.utils.GsonUtil;
import com.zh.xiche.utils.ToastUtil;

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

    private OrderEntity orderEntity;


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
        orderEntity = this.getIntent().getParcelableExtra("order");
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
        params.addBodyParameter("orderid", orderEntity.getOrderid());
        HttpUtil.http().post(params, new RequestCallBack<String>(GiveOrderActivity.this) {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Type type = new TypeToken<ResultEntity>() {
                }.getType();
                ResultEntity resultEntity = GsonUtil.GsonToBean(result, type);
                if (resultEntity.isSuccee()) {
                    ToastUtil.showShort("接单成功，转向订单详情");
                    Intent intent = new Intent(GiveOrderActivity.this, OrderDetailsActivity.class);
                    intent.putExtra("order", orderEntity);
                    intent.putExtra("type", 2);
                    startActivity(intent);
                    GiveOrderActivity.this.finish();
                } else {
                    ToastUtil.showShort("接单失败，退出，刷新订单...");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                ToastUtil.showShort(ex.getMessage());
            }
        });
    }

    /**
     * 接单
     */
    private void refuseOrder() {
        String path = HttpPath.getPath(HttpPath.ORDERREFUSER);
        RequestParams params = HttpUtil.params(path);
        params.addBodyParameter("uid", userInfoEntity.getId());
        params.addBodyParameter("tockens", userInfoEntity.getTockens());
        params.addBodyParameter("orderid", orderEntity.getOrderid());
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
                    ToastUtil.showShort("接单失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                ToastUtil.showShort(ex.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        downTimer.cancel();
    }
}
