package com.zh.xiche.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zh.xiche.R;
import com.zh.xiche.base.BaseFragment;
import com.zh.xiche.ui.myorder.MyOrderSwitch;
import com.zh.xiche.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人中心
 * Created by win7 on 2016/9/21.
 */

public class PersonFragment extends BaseFragment {
    @Bind(R.id.per_icon_img)
    CircleImageView perIconImg;
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
    private View mView;

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
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.per_account_re, R.id.per_mydrder_tv, R.id.per_mybill_tv, R.id.per_setting_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.per_account_re:
                ToastUtil.showShort(R.string.per_account);
                break;
            case R.id.per_mydrder_tv:
                ToastUtil.showShort(R.string.per_order);
                Intent intent = new Intent(activity, MyOrderSwitch.class);
                startActivity(intent);
                break;
            case R.id.per_mybill_tv:
                ToastUtil.showShort(R.string.per_bill);
                break;
            case R.id.per_setting_tv:
                ToastUtil.showShort(R.string.per_setting);
                break;
        }
    }
}
