package com.zh.xiche.ui.receive;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zh.xiche.R;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.utils.ToastUtil;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giveorder);
        ButterKnife.bind(this);

        downTimer = new CountDownTimer(60 *1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                giveorderTimedownTv.setText("" + millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                ToastUtil.showShort("未接单");
                finish();
            }
        }.start();
    }

    @OnClick({R.id.giveorder_accept_btn, R.id.giveorder_refuse_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.giveorder_accept_btn:
                break;
            case R.id.giveorder_refuse_btn:
                ToastUtil.showShort("拒单");
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        downTimer.cancel();
    }
}
