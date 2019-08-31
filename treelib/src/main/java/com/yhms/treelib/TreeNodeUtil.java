package com.yhms.treelib;

import android.util.Log;

import com.yhms.treelib.bean.ContactsBean;
import com.yhms.treelib.bean.GroupBean;
import com.yhms.treelib.bean.MemberBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @功能描述: 生成树
 * @author: 邪桑子
 * @date: 2019/8/31 15:27
 */
public class TreeNodeUtil {

    private static final String TAG = TreeNodeUtil.class.getSimpleName();

    public static List<TreeNode> getTreeNodes(List<ContactsBean> beans) {
        List<TreeNode> nodes = new ArrayList<>();
        try {
            for (ContactsBean bean : beans) {
                TreeNode<GroupBean> node = new TreeNode<>(new GroupBean(bean.getId(), bean.getLabel(), bean.getTotal()));
                initData(node, bean);
                nodes.add(node);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return nodes;
    }

    private static void initData(TreeNode<GroupBean> node, ContactsBean bean) {
        List<ContactsBean.MemberInfo> members = bean.getMembers();
        if (members != null && !members.isEmpty()) {
            for (ContactsBean.MemberInfo item : members) {
                node.addChild(new TreeNode(new MemberBean(item.getId(), item.getLabel(), item.getPhone(), item.getUserId(), item.getUserIcon(), item.getUserLevel(), item.getUserType())));
            }
        }
        List<ContactsBean> children = bean.getChildren();
        if (children != null && !children.isEmpty()) {
            for (ContactsBean child : children) {
                TreeNode<GroupBean> group = new TreeNode(new GroupBean(child.getId(), child.getLabel(), bean.getTotal()));
                node.addChild(group);
                if (child.getChildren() != null) {
                    initData(group, child);
                }
            }
        }
    }
}
