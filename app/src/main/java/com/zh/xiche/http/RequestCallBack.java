package com.zh.xiche.http;

import android.content.Context;
import android.widget.Toast;

import com.zh.xiche.utils.DialogUtil;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.x;


/**
 * 用于网络请求回调
 * Created by zcw on 2016-03-11.
 */
public class RequestCallBack<T> implements Callback.ProgressCallback<T> {


    private Context context;
    private boolean isShowDialog = true;//是否显示进度条

    public RequestCallBack(Context context) {
        this(context, false);
    }

    public RequestCallBack(Context context, boolean isShowDialog) {
        this.context = context;
        this.isShowDialog = isShowDialog;
    }


    @Override
    public void onWaiting() {

    }

    @Override
    public void onStarted() {
        if (isShowDialog) {
            DialogUtil.showProgress(context);
        }
    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {

    }

    @Override
    public void onSuccess(T result) {

    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
//Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
        if (ex instanceof HttpException) { // 网络错误
            HttpException httpEx = (HttpException) ex;
            int responseCode = httpEx.getCode();
            String responseMsg = httpEx.getMessage();
            String errorResult = httpEx.getResult();
            // ...
            Toast.makeText(x.app(), "网络错误：" + responseCode, Toast.LENGTH_LONG).show();

        } else { // 其他错误
            // ...
            if (ex.getMessage() != null){
                Toast.makeText(x.app(), "系统错误：" + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {
        DialogUtil.stopProgress(context);
    }
}
