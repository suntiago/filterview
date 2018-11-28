package com.suntiago.filterview.flowLayout;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.suntiago.filterview.R;
import com.suntiago.filterview.flowLayout.filter.FilterData;
import com.suntiago.filterview.flowLayout.filter.FilterView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by zy on 2018/9/10.
 * 选择省份
 */

public class ProvincePicker {
    private PopupWindow mPopupWindow;
    Activity mActivity;
    private String[] mProvinces;
    ProvinceCallback mProvinceCallback;

    public ProvincePicker(Activity activity, ProvinceCallback provinceCallback) {
        mActivity = activity;
        mProvinceCallback = provinceCallback;
    }

    public ProvincePicker(String[] provinces, Activity activity, ProvinceCallback provinceCallback) {
        mProvinces = provinces;
        mActivity = activity;
        mProvinceCallback = provinceCallback;
    }


    /**
     * popupwindow弹窗
     */
    public void showPopWindow() {
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        View view = LayoutInflater.from(mActivity).inflate(R.layout.popup_province_choose, null, false);
        FilterView filterView = (FilterView) view.findViewById(R.id.province_filter);
        filterView.setMultiSelect(false);
        filterView.setNotNull(false);
        List<FilterData> list = new ArrayList<>();
        if (mProvinces == null) {
            mProvinces = mActivity.getResources().getStringArray(R.array.province_array);
        }
        for (String province : mProvinces) {
            list.add(new FilterData(province, province));
        }
        filterView.initData(list, null);
        mPopupWindow = new PopupWindow(view, width, height);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        int h = getDaoHangHeight(mActivity);
        mPopupWindow.showAtLocation(getRootView(mActivity), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h);
        backgroundAlpha(0.5f);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });

        filterView.setOnItemClick(new FilterView.OnItemClick() {
            @Override
            public void onClick(boolean b, FilterData data) {
                if (mProvinceCallback != null) {
                    mProvinceCallback.pick(data.getValue());
                }
                mPopupWindow.dismiss();
            }
        });
    }

    //获取根view
    private View getRootView(Activity activity) {
        return ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
    }

    /**
     * 设置页面背景色透明度
     *
     * @param bgAlpha
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mActivity.getWindow().setAttributes(lp);
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    public static int getDaoHangHeight(Context context) {
        int resourceId = 0;
        if (checkDeviceHasNavigationBar(context)) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }

    private static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    public interface ProvinceCallback {
        void pick(String province);
    }
}
