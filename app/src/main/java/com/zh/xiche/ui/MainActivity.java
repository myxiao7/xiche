package com.zh.xiche.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zh.xiche.R;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.base.BaseApplication;
import com.zh.xiche.entity.UserInfoEntity;
import com.zh.xiche.ui.fragment.MainFragment;
import com.zh.xiche.ui.fragment.PersonFragment;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.ToastUtil;

import org.xutils.common.util.LogUtil;

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
    private TabFragmentPagerAdapter adapter;

    long waitTime = 2000;
    long touchTime = 0;
    @Override
    public void onBackPressed() {
      /*  long currentTime = System.currentTimeMillis();
        if((currentTime-touchTime)>=waitTime) {
            ToastUtil.showShort("再按一次退出");
            touchTime = currentTime;
        }else {
            finish();
        }*/
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbarTv.setText(R.string.app_name);
        entity = DbUtils.getInstance().getPersonInfo();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BaseApplication.LOGOUT);
        activity.registerReceiver(MyBroadcastReceiver, intentFilter);

        if(TextUtils.isEmpty(entity.getCardno()) || TextUtils.isEmpty(entity.getLocation()) || TextUtils.isEmpty(entity.getName())){
            ToastUtil.showShort("请先完善个人信息");
            Intent intent = new Intent(activity, ModifyUserInfoActivity.class);
            startActivity(intent);
        }
        Fragment fragment01 = MainFragment.newInstance();
        Fragment fragment02 = PersonFragment.newInstance();
        list.add(fragment01);
        list.add(fragment02);
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list);
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        DbUtils.getInstance().updateState(1);
        adapter.notifyDataSetChanged();
        if(TextUtils.isEmpty(entity.getCardno()) || TextUtils.isEmpty(entity.getLocation()) || TextUtils.isEmpty(entity.getName())){
            ToastUtil.showShort("请先完善个人信息");
            Intent intent2 = new Intent(activity, ModifyUserInfoActivity.class);
            startActivity(intent2);
        }
        LogUtil.d("接到通知");
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

    BroadcastReceiver MyBroadcastReceiver =  new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(BaseApplication.LOGOUT)){
                ToastUtil.showShort("主界面注销");
                Intent intent1 = new Intent(activity, LoginActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                activity.finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        activity.unregisterReceiver(MyBroadcastReceiver);
    }
}
