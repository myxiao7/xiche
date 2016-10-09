package com.zh.xiche.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zh.xiche.R;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.entity.UserInfoEntity;
import com.zh.xiche.ui.fragment.MainFragment;
import com.zh.xiche.ui.fragment.PersonFragment;
import com.zh.xiche.utils.DbUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by win7 on 2016/9/21.
 */

public class MainActivity extends BaseActivity {
    @Bind(R.id.toolbar_tv)
    TextView toolbarTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.main_viewpager)
    ViewPager mainViewpager;
    @Bind(R.id.main_tab01)
    RadioButton mainTab01;
    @Bind(R.id.main_tab02)
    RadioButton mainTab02;
    @Bind(R.id.main_radiogroup)
    RadioGroup mainRadiogroup;
    private List<Fragment> list = new ArrayList<>();

    private UserInfoEntity entity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbarTv.setText(R.string.app_name);
        entity = DbUtils.getInstance().getPersonInfo();
        Fragment fragment01 = MainFragment.newInstance();
        Fragment fragment02 = PersonFragment.newInstance();
        list.add(fragment01);
        list.add(fragment02);
        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list);
        mainViewpager.setAdapter(adapter);

        mainViewpager.setCurrentItem(0, false);

        mainViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1){
                    position = 2;
                }
                RadioButton radioButton = (RadioButton) mainRadiogroup.getChildAt(position);
                radioButton.setChecked(true);
                toolbarTv.setText(radioButton.getText().toString()+"");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mainRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int indexOfChild = group.indexOfChild(group.findViewById(checkedId));
                mainViewpager.setCurrentItem(indexOfChild,true);
            }
        });
    }

    private class TabFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
