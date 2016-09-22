package com.zh.xiche.ui.myorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zh.xiche.R;
import com.zh.xiche.base.BaseFragment;

/**
 * 服务中订单
 * Created by win7 on 2016/9/22.
 */

public class FragmentMyorder01 extends BaseFragment {
    private View mView;

    public static FragmentMyorder01 newInstance(int id){
        FragmentMyorder01 fragmentMyorder01= new FragmentMyorder01();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        fragmentMyorder01.setArguments(bundle);
        return fragmentMyorder01;
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
