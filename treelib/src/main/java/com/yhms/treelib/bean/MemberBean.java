package com.yhms.treelib.bean;

import com.yhms.treelib.LayoutItemType;
import com.yhms.treelib.R;

/**
 * @功能描述:
 * @author: 邪桑子
 * @date: 2019/8/30 14:40
 */
public class MemberBean implements LayoutItemType {
    private String id;
    private String name;
    private String phone;
    private String userId;
    private String userIcon;
    private int userLevel;
    private int userType;

    public MemberBean(String id, String name, String phone) {
        this(id, name, phone, "", "", -1, -1);
    }

    public MemberBean(String id, String name, String phone, String userIcon) {
        this(id, name, phone, "", userIcon, -1, -1);
    }

    public MemberBean(String id, String name, String phone, String userId, String userIcon, int userLevel, int userType) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.userId = userId;
        this.userIcon = userIcon;
        this.userLevel = userLevel;
        this.userType = userType;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_member;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
