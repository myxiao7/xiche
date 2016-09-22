package com.zh.xiche.ui.myorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zh.xiche.R;
import com.zh.xiche.base.BaseFragment;

/**
 * 待服务订单
 * Created by win7 on 2016/9/22.
 */

public class FragmentMyorder02 extends BaseFragment {
    private View mView;

    public static FragmentMyorder02 newInstance(int id){
        FragmentMyorder02 fragmentMyorder02= new FragmentMyorder02();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        fragmentMyorder02.setArguments(bundle);
        return fragmentMyorder02;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_person, container, false);
        }
        return mView;
    }
}
