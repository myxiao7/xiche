package com.zh.xiche.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.zh.xiche.R;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.utils.ImageLoaderHelper;
import com.zh.xiche.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

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
    @Bind(R.id.register_pwd_edit)
    EditText registerPwdEdit;
    @Bind(R.id.register_code_edit)
    EditText registerCodeEdit;
    @Bind(R.id.register_getcode_txt)
    TextView registerGetcodeTxt;

    private List<String> urls = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        initBanner();

    }

    /**
     * 初始化轮播
     */
    private void initBanner() {
        urls.add(0, "http://www.sinaimg.cn/dy/slidenews/2_img/2016_37/61364_1929700_451247.jpg");
        urls.add(1, "http://www.sinaimg.cn/dy/slidenews/2_img/2016_38/61364_1932867_482144.jpg");
        urls.add(2, "http://www.sinaimg.cn/dy/slidenews/2_img/2016_38/61364_1932872_251055.jpg");
        urls.add(3, "http://www.sinaimg.cn/dy/slidenews/2_img/2016_38/61364_1932867_482144.jpg");
        registerBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
            @Override
            public LocalImageHolderView createHolder() {
                return new LocalImageHolderView();
            }
        }, urls)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.ic_dot_default, R.mipmap.ic_dot_selected})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT).setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ToastUtil.showShort(urls.get(position));
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
                new CountDownTimer(60 * 1000, 1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        registerGetcodeTxt.setClickable(false);
                        registerGetcodeTxt.setText(String.format(getResources().getString(R.string.code_wait), (millisUntilFinished/1000)+""));
                    }

                    @Override
                    public void onFinish() {
                        registerGetcodeTxt.setClickable(true);
                        registerGetcodeTxt.setText(R.string.code_get);
                    }
                }.start();
            }
        });
    }

    @OnClick(R.id.register_registe_btn)
    public void onClick() {

    }


    public class LocalImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            ImageLoaderHelper.getInstance().loadPic(imageView, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
