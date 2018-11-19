package com.suntiago.filterview.flowLayout.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaiyu on 2016/8/8.
 */
public class ChoseFilter {
    boolean enableMultiSelect;
    boolean notNull;
    //1:content, 0: title
    List<FilterData> list;
    //max表示每组最大
    int max = -1;
    String mGroupChose;

    public ChoseFilter(boolean muti, boolean notnull, int max) {
        enableMultiSelect = muti;
        notNull = notnull;
        list = new ArrayList<>();
        this.max = max;
    }

    public boolean contains(FilterData nameValue) {
        if (list == null) {
            return false;
        }
        return list.contains(nameValue);
    }

    public void addNamValue(FilterData nameValue) {
        if (list == null) {
            return;
        }
        if (list.contains(nameValue)) {
            if (list.size() > 1 || !notNull) {
                list.remove(nameValue);
            }
        } else {
            if (!enableMultiSelect) {
                list.clear();
            }
            if (list.size() < max) {
                list.add(nameValue);
            }
        }
    }

    public void resetNameValue(FilterData nameValue) {
        if (list == null) {
            return;
        }
        if (!notNull) {
            list.clear();
        } else {
            list.clear();
            list.add(nameValue);
        }
    }

    public boolean isEnableMultiSelect() {
        return enableMultiSelect;
    }

    public void setEnableMultiSelect(boolean enableMultiSelect) {
        this.enableMultiSelect = enableMultiSelect;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public List<FilterData> getList() {
        return list;
    }

    public void setList(List<FilterData> list) {
        this.list = list;
    }

    public void addNamValue(List<FilterData> choseList) {
        if (choseList != null && choseList.size() != 0) {
            for (FilterData f : choseList) {
                addNamValue(f);
            }
        }
    }
}
