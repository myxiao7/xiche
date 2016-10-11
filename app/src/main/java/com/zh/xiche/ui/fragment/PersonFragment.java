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

import com.baidu.mapapi.map.Text;
import com.zh.xiche.R;
import com.zh.xiche.base.BaseFragment;
import com.zh.xiche.entity.UserInfoEntity;

import com.zh.xiche.ui.PersonInfo;
import com.zh.xiche.ui.myorder.MyOrderSwitch;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.ImageLoaderHelper;
import com.zh.xiche.utils.ToastUtil;

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
        if(!TextUtils.isEmpty(entity.getAvatar())){
            ImageLoaderHelper.getInstance().loadPic(perIconImg, entity.getAvatar());
        }
        return mView;
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
                ToastUtil.showShort(R.string.per_order);
                intent = new Intent(activity, MyOrderSwitch.class);
                startActivity(intent);
                break;
            case R.id.per_mybill_tv:
                ToastUtil.showShort(R.string.per_bill);
                /*intent = new Intent(activity, OrderDetailsActivity.class);
                startActivity(intent);*/
                break;
            case R.id.per_setting_tv:
                ToastUtil.showShort(R.string.per_setting);
                break;
        }
    }
}
