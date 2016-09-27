package com.zh.xiche.view.slider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 拼音索引
 * Created by zhanghao on 2016/8/30.
 */
public class SideBar extends View{
    //26个首字母
    public static String []INDEX_STRING = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    private List<String> indexStrings;
    private int choose = -1;//默认选中
    private Paint paint = new Paint();//画笔
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    private TextView mTextDialog;//中部显示选择的首字母
    /**
     * 为SideBar设置显示字母的TextView
     */
    public void setTextView(TextView mTextDialog){
        this.mTextDialog = mTextDialog;
    }

    public SideBar(Context context) {
        super(context);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / INDEX_STRING.length;// 获取每一个字母的高度

        for (int i = 0; i < INDEX_STRING.length; i++) {
            paint.setColor(Color.parseColor("#00BFFF"));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(20);
            // 选中的状态 变色加粗
            if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff"));
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(INDEX_STRING[i]) / 2;
            // y坐标等于当前字母高度 + 上一字母高度
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(INDEX_STRING[i], xPos, yPos, paint);
            paint.reset();// 重置画笔
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();//获取y坐标
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * indexStrings.size());// 点击y坐标所占总高度的比例*indexStrings数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.WHITE);
                choose = -1;
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.GONE);
                }
                break;
            default:
                if (oldChoose != c) {
                    if (c >= 0 && c < indexStrings.size()) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(indexStrings.get(c));
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(indexStrings.get(c));
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }

    public void setIndexText(ArrayList<String> indexStrings) {
        this.indexStrings = indexStrings;
        invalidate();
    }

    /**
     * 公开的的方法
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 回调接口
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }
}
