package com.yhms.treelib.binder;

import android.view.View;

import com.yhms.treelib.TreeNode;

public interface OnItemListener {
    boolean onClick(TreeNode node, View itemView, int position);
}