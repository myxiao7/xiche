package com.zh.xiche.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by win7 on 2016/9/18.
 */
public class BaseActivity extends AppCompatActivity {
    protected BaseActivity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        Log.d(BaseApplication.LOG_TAG,getClass().getName());
    }
}
