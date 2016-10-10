package com.zh.xiche.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zh.xiche.R;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.config.HttpPath;
import com.zh.xiche.entity.BaseEditEnum;
import com.zh.xiche.entity.ResultEntity;
import com.zh.xiche.entity.UserInfoEntity;
import com.zh.xiche.http.HttpUtil;
import com.zh.xiche.http.RequestCallBack;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.GsonUtil;
import com.zh.xiche.utils.ImageLoaderHelper;
import com.zh.xiche.utils.ToastUtil;
import com.zh.xiche.utils.UILImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 个人中心
 * Created by zhanghao on 2016/10/10.
 */

public class PersonInfo extends BaseActivity {
    @Bind(R.id.toolbar_tv)
    TextView toolbarTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.person_info_icon_img)
    ImageView personInfoIconImg;
    @Bind(R.id.person_info_icon_lin)
    LinearLayout personInfoIconLin;
    @Bind(R.id.person_info_account_txt)
    TextView personInfoAccountTxt;
    @Bind(R.id.person_info_account_lin)
    LinearLayout personInfoAccountLin;
    @Bind(R.id.person_info_name_txt)
    TextView personInfoNameTxt;
    @Bind(R.id.person_info_name_lin)
    LinearLayout personInfoNameLin;
    @Bind(R.id.person_info_card_txt)
    TextView personInfoCardTxt;
    @Bind(R.id.person_info_card_lin)
    LinearLayout personInfoCardLin;
    @Bind(R.id.person_info_add_txt)
    TextView personInfoAddTxt;
    @Bind(R.id.person_info_add_lin)
    LinearLayout personInfoAddLin;

    private static final int REQUEST_EDIT = 0x1002;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;

    private UserInfoEntity entity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personinfo);
        init();
        initImageLoader(activity);
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
        toolbarTv.setText("账号管理");
        entity = DbUtils.getInstance().getPersonInfo();
        if(!TextUtils.isEmpty(entity.getAvatar())){
            ImageLoaderHelper.getInstance().loadPic(personInfoIconImg, entity.getAvatar());
        }
        personInfoAccountTxt.setText(entity.getMobile());
        personInfoNameTxt.setText(entity.getName());
        personInfoCardTxt.setText(entity.getCardno());
        personInfoAddTxt.setText(entity.getLocation());
    }

    @OnClick({R.id.person_info_icon_lin, R.id.person_info_account_lin, R.id.person_info_name_lin, R.id.person_info_card_lin, R.id.person_info_add_lin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.person_info_icon_lin:
                final FunctionConfig functionConfig = getFunctionConfig();
                //修改头像
                new MaterialDialog.Builder(this)
                        .title("头像")
                        .items(new String[]{"拍照", "从相册选择"})
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {
                                    case 0:
                                        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
                                        break;
                                    case 1:
                                        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
                                        break;
                                }
                            }
                        })
                        .show();
                break;
            case R.id.person_info_account_lin:
                break;
            case R.id.person_info_name_lin:
                startEdit(BaseEditEnum.perinfo_name.getId(), personInfoNameTxt.getText().toString());
                break;
            case R.id.person_info_card_lin:
                startEdit(BaseEditEnum.perinfo_card.getId(), personInfoCardTxt.getText().toString());
                break;
            case R.id.person_info_add_lin:
                startEdit(BaseEditEnum.perinfo_add.getId(), personInfoAddTxt.getText().toString());
                break;
        }
    }

    private void startEdit(int id, String str) {
        Intent intent = new Intent(activity, CommonEdit.class);
        //传入枚举ID
        intent.putExtra(CommonEdit.REQTYPE, id);
        //传入原字符串
        intent.putExtra(CommonEdit.BASESTR, str);
        startActivity(intent);
    }

    /**
     * 初始化相册
     * @return
     */
    private FunctionConfig getFunctionConfig() {
        //设置主题
        ThemeConfig theme = ThemeConfig.DEFAULT;
        //配置功能
        final FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropHeight(1024)
                .setCropWidth(1024)
                .setCropSquare(true)
                .setEnablePreview(true)
                .setForceCrop(true)
                .setForceCropEdit(true)
                .build();
        cn.finalteam.galleryfinal.ImageLoader imageLoader = new UILImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(activity, imageLoader, theme)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
        return functionConfig;
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                String path = resultList.get(0).getPhotoPath();
                UploadAvatar(path);
//                mPhotoList.addAll(resultList);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(activity, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };


    private void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());

    }

    /**
     * 修改用户头像
     *
     * @param avatarPath
     */
    private void UploadAvatar(final String avatarPath) {
        String url = HttpPath.getPath(HttpPath.MODIFYICON);
        RequestParams params = HttpUtil.params(url);
        params.addBodyParameter("uid", entity.getId());
        params.addBodyParameter("tockens", entity.getTockens());
        params.setMultipart(true);
        params.addBodyParameter("avatar", new File(avatarPath));
        HttpUtil.http().post(params, new RequestCallBack<String>(activity) {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                LogUtil.d(result);
                Type type = new TypeToken<ResultEntity>(){}.getType();
                ResultEntity entity = GsonUtil.GsonToBean(result, type);
                if(entity.isSuccee()){
                    ToastUtil.showShort("修改成功");
                }else{
                    ToastUtil.showShort("修改失败");
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            if(requestCode == REQUEST_EDIT){
                int id = data.getIntExtra(CommonEdit.REQTYPE, 1);
                switch (id){
                    case 1:
                        personInfoNameTxt.setText(data.getStringExtra(CommonEdit.RESULTSTR));
                        break;
                    case 2:
                        personInfoCardTxt.setText(data.getStringExtra(CommonEdit.RESULTSTR));
                        break;
                    case 3:
                        personInfoAddTxt.setText(data.getStringExtra(CommonEdit.RESULTSTR));
                        break;

                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
