package com.zh.xiche.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.zh.xiche.R;


public class DialogUtil {
    static ProgressDialog myDialog;

    public static ProgressDialog showProgress(Context context) {
        if (!(myDialog != null && myDialog.isShowing())) {
            myDialog = ProgressDialog.show(context, null,
                    context.getString(R.string.loading));
            myDialog.setCanceledOnTouchOutside(true);
        }
        return myDialog;
    }
    public static void stopProgress(Context context) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.dismiss();
        }
    }
}
