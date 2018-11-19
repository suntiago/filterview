package com.suntiago.filterview.flowLayout.filter;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.suntiago.filterview.R;

import java.util.List;


/**
 * Created by zaiyu on 2016/8/8.
 */
public class FilterView extends AutoLineFeedViewGroup implements View.OnClickListener {
    //多选最大选择数
    private int mMaxChose = 3;
    //允许多选
    private boolean mMultiSelect = false;
    //是否不为空
    private boolean mNotNull = true;
    //记录已选的
    private ChoseFilter mChoseFilter;
    //点击事件回传activity
    private OnItemClick onItemClick;

    //标题 文字的颜色
    int titleTextColor;
    //标题 文字的大小
    int titleTextSize;
    //内容 文字的颜色
    int contentTextColor;
    //内容 文字的选中时的颜色
    int contentTextColorSelect;

    //内容 文字的大小
    int contentTextSize;
    //内容 文字的背景
    int contentTextBackgroundRes;

    //内容的宽度， 设置autoSquare时此项无效
    int contentTextWidth;
    //内容的高度， 设置autoSquare时此项无效
    int contentTextHeight;

    //每个数据单元的间距
    int contentTextMargin;

    //是否设置显示单元为正方形， 否则是自适应的长度
    boolean autoSquare;

    public FilterView(Context context) {
        super(context);
        initView(context, null);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.FilterView);
        titleTextColor = typedArray.getColor(R.styleable.FilterView_titleTextColorRes,
                getResources().getColor(R.color.filter_text_color_property));
        titleTextSize = typedArray.getDimensionPixelSize(R.styleable.FilterView_titleTextSize, 30);
        contentTextColor = typedArray.getResourceId(R.styleable.FilterView_contentTextColorRes,
                getResources().getColor(R.color.filter_text_color_property));

        contentTextColorSelect = typedArray.getColor(R.styleable.FilterView_contentTextColorSelectRes,
                getResources().getColor(R.color.filter_text_color_property_select));

        contentTextSize = typedArray.getDimensionPixelSize(R.styleable.FilterView_contentTextSize, 30);
        contentTextWidth = typedArray.getDimensionPixelSize(R.styleable.FilterView_contentTextWidth, 0);
        contentTextHeight = typedArray.getDimensionPixelSize(R.styleable.FilterView_contentTextHeight, 0);
        autoSquare = typedArray.getBoolean(R.styleable.FilterView_autoSquare, false);
        boolean gravityCentre = typedArray.getBoolean(R.styleable.FilterView_gravityCentre, false);
        setGravityCentre(gravityCentre);
        if (autoSquare) {
            contentTextWidth = contentTextSize + 40;
            contentTextHeight = contentTextWidth;
        }
        contentTextMargin = typedArray.getDimensionPixelSize(R.styleable.FilterView_contentTextMargin, 24);
        setGAPHORI(contentTextMargin);
        setGAPVER(contentTextMargin);
        contentTextBackgroundRes = typedArray.getResourceId(
                R.styleable.FilterView_contentTextBackgroundRes, R.drawable.type_choose_bg);
        mMultiSelect = typedArray.getBoolean(R.styleable.FilterView_multiSelect, false);
        mMaxChose = typedArray.getInt(R.styleable.FilterView_maxChose, 3);
        mNotNull = typedArray.getBoolean(R.styleable.FilterView_notNull, true);
        typedArray.recycle();
    }

    public void initData(List<FilterData> datas, List<FilterData> choseList) {
        if (datas != null && datas.size() != 0) {
            mChoseFilter = new ChoseFilter(mMultiSelect, mNotNull, mMaxChose);
            mChoseFilter.addNamValue(choseList);
            removeAllViews();
            for (FilterData filterData : datas) {
                if (FilterData.VALUE.equals(filterData.getType())) {
                    addView(getTextView(filterData, mChoseFilter, contentTextSize,
                            contentTextBackgroundRes, contentTextColor));
                } else if (FilterData.TITLE.equals(filterData.getType())) {
                    addView(getTextViewTitle(filterData, titleTextSize, titleTextColor));
                }
            }
        }
    }

    private TextView getTextView(FilterData filterData, ChoseFilter choseFilter,
                                 int textSize, int backRes, int textColor) {
        TextView textView = new TextView(getContext());
        textView.setText(filterData.getName());
        if (contentTextHeight != 0) {
            textView.setHeight(contentTextHeight);
        }
        if (contentTextWidth != 0) {
            textView.setWidth(contentTextWidth);
        }
        textView.setTag(filterData);
        textView.setTextColor(textColor);
        textView.setBackgroundResource(backRes);
        if (!autoSquare) {
            textView.setPadding(20, 15, 20, 15);
        }
        if (choseFilter.contains(filterData)) {
            textView.setSelected(true);
        } else {
            textView.setSelected(false);
        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setOnClickListener(this);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    /**
     * 将dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    private TextView getTextViewTitle(FilterData filterData, int textSize, int textColor) {
        TextView textView = new TextView(getContext());
        textView.setText(filterData.getName() + "：");
        textView.setHeight(dip2px(getContext(), 24));
        textView.setWidth(getScreenWidth(getContext()) - dip2px(getContext(), 90));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setTextColor(textColor);
        textView.setBackgroundResource(R.color.filter_text_bg_color);
        return textView;
    }

    @Override
    public void onClick(View v) {
        FilterData filterData = (FilterData) v.getTag();
        if (filterData != null) {
            mChoseFilter.addNamValue(filterData);
        } else {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            TextView textView = (TextView) getChildAt(i);
            FilterData data = (FilterData) textView.getTag();
            if (data != null && FilterData.VALUE.equals(data.getType())) {
                if (mChoseFilter.contains(data)) {
                    textView.setSelected(true);
                    textView.setTextColor(contentTextColorSelect);
                    if (filterData == data && null != onItemClick) {
                        onItemClick.onClick(true, data);
                    }
                } else {
                    textView.setSelected(false);
                    textView.setTextColor(contentTextColor);
                    if (filterData == data && null != onItemClick) {
                        onItemClick.onClick(false, data);
                    }
                }
            }
        }
    }

    public void clear() {
        mChoseFilter.resetNameValue(null);
        for (int i = 0; i < getChildCount(); i++) {
            TextView textView = (TextView) getChildAt(i);
            textView.setSelected(false);
        }
    }

    public interface OnItemClick {
        void onClick(boolean b, FilterData data);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public List<FilterData> getSelected() {
        return mChoseFilter.getList();
    }

    public int getContentTextBackgroundRes() {
        return contentTextBackgroundRes;
    }

    public void setContentTextBackgroundRes(int contentTextBackgroundRes) {
        this.contentTextBackgroundRes = contentTextBackgroundRes;
    }

    public int getContentTextSize() {
        return contentTextSize;
    }

    public void setContentTextSize(int contentTextSize) {
        this.contentTextSize = contentTextSize;
    }

  /*  public int getContentTextColor() {
        return contentTextColor;
    }

    public void setContentTextColor(int contentTextColor) {
        this.contentTextColor = contentTextColor;
    }
*/
    public int getTitleTextSize() {
        return titleTextSize;
    }

    public void setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }

    public void setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
    }

    private ChoseFilter getChoseFilter() {
        return mChoseFilter;
    }

    private void setChoseFilter(ChoseFilter mChoseFilter) {
        this.mChoseFilter = mChoseFilter;
    }

    public boolean isNotNull() {
        return mNotNull;
    }

    public void setNotNull(boolean mNotNull) {
        this.mNotNull = mNotNull;
    }

    public boolean isMultiSelect() {
        return mMultiSelect;
    }

    public void setMultiSelect(boolean mMultiSelect) {
        this.mMultiSelect = mMultiSelect;
    }

    public int getMaxChose() {
        return mMaxChose;
    }

    public void setMaxChose(int mMaxChose) {
        this.mMaxChose = mMaxChose;
    }
}
