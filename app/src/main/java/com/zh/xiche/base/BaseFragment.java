package com.zh.xiche.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by win7 on 2016/9/18.
 */
public class BaseFragment extends Fragment {
    protected BaseActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (BaseActivity) this.getActivity();
        Log.d(BaseApplication.LOG_TAG, getClass().getName());
    }
}
