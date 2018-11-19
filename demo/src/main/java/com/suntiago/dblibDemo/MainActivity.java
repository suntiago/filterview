package com.suntiago.dblibDemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.suntiago.filterview.flowLayout.ProvincePicker;
import com.suntiago.filterview.flowLayout.filter.FilterData;
import com.suntiago.filterview.flowLayout.filter.FilterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2018/11/16.
 */

public class MainActivity extends Activity {
    private final String TAG = getClass().getSimpleName();

    TextView tvProvince;
    FilterView fvPostFilter;
    TextView tvColorChose;
    FilterView fvPostFilterSquare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvProvince = (TextView) findViewById(R.id.tv_province);
        fvPostFilter = findViewById(R.id.post_filter);
        tvColorChose = findViewById(R.id.tv_color_chose);
        fvPostFilterSquare = findViewById(R.id.post_filter_square);
        initFilterColor();
        initFilterColorsquare();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void clickOnFilterProvince(View view) {
        ProvincePicker provincePicker = new ProvincePicker(this, new ProvincePicker.ProvinceCallback() {
            @Override
            public void pick(String province) {
                tvProvince.setText(province);
            }
        });
        provincePicker.showPopWindow();
    }

    public void clickOnFilterColor(View view) {
        List<FilterData> filterData = fvPostFilter.getSelected();
        String chose = "";
        if (filterData != null) {
            for (FilterData filterDatum : filterData) {
                chose += filterDatum.getName() + " ";
            }
        }
        tvColorChose.setText(chose);
    }


    public void initFilterColor() {
        List<FilterData> list = new ArrayList<>();
        list.add(new FilterData("白色", "101"));
        list.add(new FilterData("黑色", "102"));
        list.add(new FilterData("咖啡色", "103"));
        list.add(new FilterData("黑白条纹1", "104"));
        list.add(new FilterData("白色1", "105"));
        list.add(new FilterData("黑色1", "106"));
        list.add(new FilterData("咖啡色2", "107", false));
        list.add(new FilterData("黑白条纹3", "108", false));
        list.add(new FilterData("白色3", "1012"));
        list.add(new FilterData("黑色4", "1033"));
        list.add(new FilterData("咖啡色5", "1043"));
        list.add(new FilterData("黑白条纹6", "1054"));
        fvPostFilter.setMultiSelect(true);
        fvPostFilter.setMaxChose(3);
        fvPostFilter.setNotNull(false);
        fvPostFilter.initData(list, null);
    }

    public void initFilterColorsquare() {
        List<FilterData> list = new ArrayList<>();
        list.add(new FilterData("白色", "101"));
        list.add(new FilterData("黑色", "102"));
        list.add(new FilterData("咖啡色", "103"));
        list.add(new FilterData("黑白条纹1", "104"));
        list.add(new FilterData("白色1", "105"));
        list.add(new FilterData("黑色1", "106"));
        list.add(new FilterData("咖啡色2", "107"));
        list.add(new FilterData("黑白条纹3", "108"));
        list.add(new FilterData("白色3", "1012"));
        list.add(new FilterData("黑色4", "1033"));
        list.add(new FilterData("咖啡色5", "1043"));
        list.add(new FilterData("黑白条纹6", "1054"));
        fvPostFilterSquare.setMultiSelect(false);
        fvPostFilterSquare.setNotNull(false);
        fvPostFilterSquare.initData(list, null);
    }

}
