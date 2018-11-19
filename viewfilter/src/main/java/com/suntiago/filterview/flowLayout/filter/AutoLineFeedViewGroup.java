package com.suntiago.filterview.flowLayout.filter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Created by yu.zai on 2015/12/30.
 * child view will auto line feed
 */
public class AutoLineFeedViewGroup extends ViewGroup {
    private int GAP_VER = 24;
    private int GAP_HORI = 24;

    private boolean gravityCentre = true;

    /**
     * @param context
     */
    public AutoLineFeedViewGroup(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public AutoLineFeedViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * @param context
     * @param attrs
     */
    public AutoLineFeedViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setGAPVER(int ver) {
        GAP_VER = ver;
    }

    public void setGAPHORI(int hori) {
        GAP_HORI = hori;
    }

    public void setGravityCentre(boolean gravityCentre) {
        this.gravityCentre = gravityCentre;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int x = 0;// 横坐标开始
        int y = 0;//纵坐标开始
        int rows = 1;
        //记录本行的循环记录
        int currentLineStartIndex = 0;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
            //判断是否需要换行
            if (currentLineStartIndex >= countItem.get(rows)) {
                rows++;
                currentLineStartIndex = 0;
            }
            if (currentLineStartIndex == 0) {
                //左右padding
                int paddingLR = 0;
                Integer coEnd = mEndRange.get(rows);
                if (gravityCentre && coEnd != null) {
                    paddingLR = coEnd / 2;
                }
                x = paddingLR;
            }
            y = rows * (height + GAP_VER);
            view.layout(x, y - height, width + x, y);
            currentLineStartIndex++;
            x += GAP_HORI + width;
        }
    }

    HashMap<Integer, Integer> countItem = new HashMap<>();
    HashMap<Integer, Integer> mEndRange = new HashMap<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int x = 0;//横坐标
        int y = 0;//纵坐标
        int rows = 0;//总行数
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int actualWidth = specWidth /*- GAP_HORI * 2*/;//实际宽度
        int childCount = getChildCount();
        //记录每一行的个数
        int countitemsPerLine = 0;
        //剩余的空白宽度
        int lastBlank = 0;
        for (int index = 0; index < childCount; ) {
            View child = getChildAt(index);
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            //最后的空白不足以放下
            if (rows < 1 || lastBlank < (width + GAP_HORI * 2)) {//换行
                //换行
                rows++;
                //重置换行起始位置
                x = 0;
                //重置每行个数
                countitemsPerLine = 0;
            }
            countitemsPerLine++;
            int itemWidth_GAP = GAP_HORI + width;
            if (countitemsPerLine == 1) {
                x += width;
            } else {
                x += itemWidth_GAP;
            }
            lastBlank = actualWidth - x;
            index++;
            if (rows > 0) {
                //记录上一行child个数
                if (countItem.containsKey(rows)) {
                    countItem.remove(rows);
                }
                countItem.put(rows, countitemsPerLine);
                //记录上一行多余的空白
                if (mEndRange.containsKey(rows)) {
                    mEndRange.remove(rows);
                }
                mEndRange.put(rows, lastBlank);
            }
            y = rows * (height + GAP_VER);
        }
        setMeasuredDimension(specWidth, y + GAP_VER);
    }

}