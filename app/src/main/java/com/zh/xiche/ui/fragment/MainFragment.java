package com.zh.xiche.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.SupportMapFragment;
import com.zh.xiche.R;
import com.zh.xiche.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页
 * Created by win7 on 2016/9/21.
 */

public class MainFragment extends BaseFragment {
    @Bind(R.id.bmapView)
    MapView bmapView;
    @Bind(R.id.frag_main_list_tv)
    TextView fragMainListTv;
    @Bind(R.id.frag_main_list2_tv)
    TextView fragMainList2Tv;
    private View mView;
    MapView mMapView = null;
    SupportMapFragment map;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("", 1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_main, container, false);
        }
        ButterKnife.bind(this, mView);
        map = SupportMapFragment.newInstance();
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.frag_main_list_tv, R.id.frag_main_list2_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frag_main_list_tv:
                break;
            case R.id.frag_main_list2_tv:
                break;
        }
    }
}
