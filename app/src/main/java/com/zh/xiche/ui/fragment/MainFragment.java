package com.zh.xiche.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zh.xiche.R;
import com.zh.xiche.base.BaseFragment;

/**
 * 首页
 * Created by win7 on 2016/9/21.
 */

public class MainFragment extends BaseFragment {
    private View mView;

    public static MainFragment newInstance(){
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("",1);
        fragment.setArguments(bundle);
        return  fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null ){
            mView = inflater.inflate(R.layout.fragment_main, container, false);
        }
        return mView;
    }
}
