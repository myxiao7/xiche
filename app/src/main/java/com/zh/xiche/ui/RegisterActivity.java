package com.zh.xiche.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.b.e;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.google.gson.reflect.TypeToken;
import com.zh.xiche.R;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.entity.ResultEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.DialogUtils;
import com.zh.xiche.utils.GsonUtil;
import com.zh.xiche.utils.ImageLoaderHelper;
import com.zh.xiche.utils.ToastUtil;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by win7 on 2016/9/19.
 */
public class RegisterActivity extends BaseActivity {
    @Bind(R.id.toolbar_tv)
    TextView toolbarTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.register_banner)
    ConvenientBanner registerBanner;
    @Bind(R.id.register_checkBox)
    CheckBox registerCheckBox;
    @Bind(R.id.register_registe_btn)
    Button registerRegisteBtn;
    @Bind(R.id.register_name_edit)
    EditText registerNameEdit;
    @Bind(R.id.register_name_check)
    ImageView registerNameCheck;
    @Bind(R.id.register_pwd_edit)
    EditText registerPwdEdit;
    @Bind(R.id.register_code_edit)
    EditText registerCodeEdit;
    @Bind(R.id.register_getcode_txt)
    TextView registerGetcodeTxt;


    private List<String> urls = new ArrayList<>();
    private List<Integer> res = new ArrayList<>();
    private CountDownTimer downTimer;

    private boolean isUsed = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        init();
        initBanner();
//        getBill();

    }

    /**
     * 初始化轮播
     */
    private void initBanner() {
        res.add(R.mipmap.banner01);
        res.add(R.mipmap.banner01);
        /*urls.add(2, "http://www.sinaimg.cn/dy/slidenews/2_img/2016_38/61364_1932872_251055.jpg");
        urls.add(3, "http://www.sinaimg.cn/dy/slidenews/2_img/2016_38/61364_1932867_482144.jpg");*/
        registerBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
            @Override
            public LocalImageHolderView createHolder() {
                return new LocalImageHolderView();
            }
        }, res)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.ic_dot_default, R.mipmap.ic_dot_selected})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT).setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                ToastUtil.showShort(urls.get(position));
            }
        })
                .startTurning(2500);
    }

    private void init() {
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        toolbarTv.setText("注册");
        registerCheckBox.setText(Html.fromHtml(getResources().getString(R.string.register_check)));
        registerGetcodeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(registerNameEdit.getText().toString())) {
                    ToastUtil.showShort(R.string.login_phone_hint);
                    return;
                }

                if (!isMobile(registerNameEdit.getText().toString())) {
                    ToastUtil.showShort(R.string.login_vai_hint);
                    return;
                }
                getCode(registerNameEdit.getText().toString());
            }
        });

        registerNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isMobile(s.toString().trim())){
                   /* //检查用户名是否注册
                    ToastUtil.showShort("开始检查手机号");
                    checkUserName(s.toString().trim());*/
                }
            }
        });
    }

    /**
     * 获取验证码
     * @param phone
     */
    private void getCode(String phone) {
        String url = HttpPath.getPath(HttpPath.GETCODE);
        RequestParams params = HttpUtil.params(url);
        params.addBodyParameter("mobile", phone);
        HttpUtil.http().post(params, new RequestCallBack<String>(activity){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);
                Type type = new TypeToken<ResultEntity>(){}.getType();
                ResultEntity entity = GsonUtil.GsonToBean(result, type);
                if(entity.isSuccee()){
                    ToastUtil.showShort("验证码以发送");
                    registerGetcodeTxt.setClickable(false);
                    downTimer = new CountDownTimer(60 * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            registerGetcodeTxt.setText(String.format(getResources().getString(R.string.code_wait), (millisUntilFinished / 1000) + ""));
                        }

                        @Override
                        public void onFinish() {
                            registerGetcodeTxt.setClickable(true);
                            registerGetcodeTxt.setText(R.string.code_get);
                        }
                    };
                    downTimer.start();
                }else{
                    ToastUtil.showShort("验证码获取失败，请检查手机号码");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                ToastUtil.showShort(ex.getMessage());
            }
        });

    }

    /**
     * 注册
     */
    private void register() {
        String url = HttpPath.getPath(HttpPath.REGISTER);
        RequestParams params = HttpUtil.params(url);
        params.addBodyParameter("mobile", registerNameEdit.getText().toString());
        params.addBodyParameter("password", registerPwdEdit.getText().toString());
        params.addBodyParameter("code", registerCodeEdit.getText().toString());
        HttpUtil.http().post(params, new RequestCallBack<String>(activity){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                DialogUtils.stopProgress(activity);
                LogUtil.d(result);
                Type type = new TypeToken<ResultEntity>(){}.getType();
                ResultEntity entity = GsonUtil.GsonToBean(result, type);
                if(entity.isSuccee()){
                    ToastUtil.showShort("注册成功");
                    //完善资料
                    Intent intent = new Intent(activity, RegisterUserInfoActivity.class);
                    intent.putExtra("id", entity.getOperatorDTO().getId());
                    intent.putExtra("token",entity.getOperatorDTO().getTockens());
                    intent.putExtra("userName",registerNameEdit.getText().toString());
                    intent.putExtra("userPwd",registerPwdEdit.getText().toString());
                    startActivity(intent);
                    activity.finish();
                }else{
                    ToastUtil.showShort("注册失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                DialogUtils.stopProgress(activity);
                ToastUtil.showShort(ex.getMessage());
            }
        });

    }

    /**
     * 注册
     */
    private void getBill() {
        String url = HttpPath.getPath(HttpPath.BILLBYALL);
        RequestParams params = HttpUtil.params(url);
        params.addBodyParameter("operid", "130");
        params.addBodyParameter("tockens", "a493644dea74475885b40f0081035606");
        params.addBodyParameter("rows", "10");
        params.addBodyParameter("page", "0");
        /*params.addBodyParameter("sidx", "");
        params.addBodyParameter("sord", "asc");*/
        HttpUtil.http().get(params, new RequestCallBack<String>(activity){
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                DialogUtils.stopProgress(activity);
                LogUtil.d(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                DialogUtils.stopProgress(activity);
                ToastUtil.showShort(ex.getMessage());
            }
        });

    }


    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    @OnClick(R.id.register_registe_btn)
    public void onClick() {
        if (TextUtils.isEmpty(registerNameEdit.getText().toString())) {
            ToastUtil.showShort(R.string.login_phone_hint);
            return;
        }
        if (TextUtils.isEmpty(registerPwdEdit.getText().toString())) {
            ToastUtil.showShort(R.string.login_pwd_hint);
            return;
        }
        if (TextUtils.isEmpty(registerCodeEdit.getText().toString())) {
            ToastUtil.showShort(R.string.code);
            return;
        }
        if (!registerCheckBox.isChecked()) {
            ToastUtil.showShort(R.string.login_notice_hint);
            return;
        }
        DialogUtils.showProgress(activity);
        register();
    }


    public class LocalImageHolderView implements Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, Integer data) {
//            ImageLoaderHelper.getInstance().loadPic(imageView, data);
            imageView.setBackgroundResource(data);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        registerBanner.stopTurning();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (downTimer != null) {
            downTimer.cancel();
        }
    }
}
