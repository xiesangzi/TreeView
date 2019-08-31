package com.yhms.treelib.bean;

import com.yhms.treelib.LayoutItemType;
import com.yhms.treelib.R;

/**
 * @功能描述:
 * @author: 邪桑子
 * @date: 2019/8/30 14:40
 */
public class GroupBean implements LayoutItemType {
    private String id;
    private String label;
    private int total;

    public GroupBean(String id, String label) {
        this(id, label, 0);
    }

    public GroupBean(String id, String label, int total) {
        this.id = id;
        this.label = label;
        this.total = total;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_group;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
