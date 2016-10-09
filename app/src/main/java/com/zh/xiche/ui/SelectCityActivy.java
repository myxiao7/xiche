package com.zh.xiche.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zh.xiche.R;
import com.zh.xiche.adapter.SelectCityAdapter;
import com.zh.xiche.base.BaseActivity;
import com.zh.xiche.entity.CitySortModel;
import com.zh.xiche.utils.ToastUtil;
import com.zh.xiche.view.slider.CharacterParser;
import com.zh.xiche.view.slider.PinyinComparator;
import com.zh.xiche.view.slider.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by win7 on 2016/9/28.
 */

public class SelectCityActivy extends BaseActivity {
    @Bind(R.id.toolbar_tv)
    TextView toolbarTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.selectcity_listview)
    ListView selectcityListview;
    @Bind(R.id.selectcity_dialog)
    TextView selectcityDialog;
    @Bind(R.id.sidrbar)
    SideBar sidrbar;


    private static final String TAG = "SelectCityActivy";
    private SelectCityAdapter adapter;
    private CharacterParser characterParser;
    private List<CitySortModel> SourceDateList;

    private String selCity = "";//所选的省市区
    private String selCityID = "";//所选的省市区ID

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcity);
        init();
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
        toolbarTv.setText("选择省市");

        sidrbar.setTextView(selectcityDialog);
        characterParser = CharacterParser.getInstance();
        sidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    selectcityListview.setSelection(position);
                }
            }
        });

        selectcityListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ToastUtil.showShort(SourceDateList.get(position).getRegion());
                selCity = selCity + SourceDateList.get(position).getRegion();
                Intent intent = new Intent();
                intent.putExtra("city", selCity);
                setResult(RESULT_OK, intent);
                activity.finish();
            }
        });

        SourceDateList = filledData(getResources().getStringArray(R.array.provinces));
        Collections.sort(SourceDateList, new PinyinComparator());
        adapter = new SelectCityAdapter(activity, SourceDateList);
        selectcityListview.setAdapter(adapter);
    }


    private List<CitySortModel> filledData(String[] date) {
        List<CitySortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            CitySortModel sortModel = new CitySortModel();
            sortModel.setRegion(date[i]);
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {

                //对重庆多音字做特殊处理
                if (pinyin.startsWith("zhongqing")) {
                    sortString = "C";
                    sortModel.setSortLetters("C");
                } else {
                    sortModel.setSortLetters(sortString.toUpperCase());
                }

                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }

            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        sidrbar.setIndexText(indexString);
        return mSortList;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
