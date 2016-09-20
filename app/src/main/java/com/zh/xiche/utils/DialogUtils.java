package com.zh.xiche.utils;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.zh.xiche.R;

/**
 * Created by zhanghao on 2016/8/26.
 */
public class DialogUtils {
    static MaterialDialog dialog;

    /**
     * 开始加载
     * @param context
     */
    public static void showProgress(Context context, int str) {
        if (!(dialog != null && dialog.isShowing())) {
            dialog = new MaterialDialog.Builder(context)
                    .progress(true, 0)
                    .content(str)
                    .progressIndeterminateStyle(false)
                    .cancelable(false)
                    .show();
        } else {
            dialog.dismiss();
        }
    }

    /**
     * 开始加载
     * @param context
     */
    public static void showProgress(Context context) {
        if (!(dialog != null && dialog.isShowing())) {
            dialog = new MaterialDialog.Builder(context)
                    .progress(true, 0)
                    .content(R.string.loading)
                    .progressIndeterminateStyle(false)
                    .cancelable(true)
                    .show();
        } else {
            dialog.dismiss();
        }
    }

    /**
     * 停止等待条
     * @param context
     */
    public static void stopProgress(Context context) {
        if ((dialog != null && dialog.isShowing())) {
            dialog.dismiss();
        }
    }
}