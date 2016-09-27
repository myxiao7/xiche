package com.zh.xiche.view.slider;

/**
 * 排序
 * Created by zhanghao on 2016/8/30.
 */

import com.zh.xiche.entity.CitySortModel;

import java.util.Comparator;

public class PinyinComparator implements Comparator<CitySortModel> {

    public int compare(CitySortModel o1, CitySortModel o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}
