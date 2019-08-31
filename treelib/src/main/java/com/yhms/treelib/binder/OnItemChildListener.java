package com.yhms.treelib.binder;

import android.view.View;

import com.yhms.treelib.TreeNode;

public interface OnItemChildListener {
    boolean onClick(TreeNode node, View view, int position);
}