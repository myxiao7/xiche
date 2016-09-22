package com.zh.xiche.ui.myorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zh.xiche.R;
import com.zh.xiche.base.BaseFragment;

/**
 * 已服务订单
 * Created by win7 on 2016/9/22.
 */

public class FragmentMyorder03 extends BaseFragment {
    private View mView;

    public static FragmentMyorder03 newInstance(int id){
        FragmentMyorder03 fragmentMyorder03= new FragmentMyorder03();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        fragmentMyorder03.setArguments(bundle);
        return fragmentMyorder03;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_main, container, false);
        }
        return mView;
    }
}
