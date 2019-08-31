package com.yhms.treelib;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：树节点
 *
 * @author 邪桑子
 * @date 2019/8/31 10:16
 */
public class TreeNode<T extends LayoutItemType> implements Cloneable {
    private T content;
    private TreeNode parent;
    private List<TreeNode> children;
    private boolean isExpand;
    private boolean isLocked;
    private int height = UNDEFINE;

    private static final int UNDEFINE = -1;

    public TreeNode(@NonNull T content) {
        this.content = content;
        this.children = new ArrayList<>();
    }

    public int getHeight() {
        if (isRoot()) {
            height = 0;
        } else if (height == UNDEFINE) {
            height = parent.getHeight() + 1;
        }
        return height;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }

    public void setContent(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children.clear();
        for (TreeNode treeNode : children) {
            addChild(treeNode);
        }
    }

    public TreeNode addChild(TreeNode node) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(node);
        node.parent = this;
        return this;
    }

    public boolean toggle() {
        isExpand = !isExpand;
        return isExpand;
    }

    public void collapse() {
        if (isExpand) {
            isExpand = false;
        }
    }

    public void collapseAll() {
        if (children == null || children.isEmpty()) {
            return;
        }
        for (TreeNode child : this.children) {
            child.collapseAll();
        }
    }

    public void expand() {
        if (!isExpand) {
            isExpand = true;
        }
    }

    public void expandAll() {
        expand();
        if (children == null || children.isEmpty()) {
            return;
        }
        for (TreeNode child : this.children) {
            child.expandAll();
        }
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public TreeNode getParent() {
        return parent;
    }

    public TreeNode<T> lock() {
        isLocked = true;
        return this;
    }

    public TreeNode<T> unlock() {
        isLocked = false;
        return this;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "content=" + this.content +
                ", parent=" + (parent == null ? "null" : parent.getContent().toString()) +
                ", children=" + (children == null ? "null" : children.toString()) +
                ", isExpand=" + isExpand +
                '}';
    }

    @Override
    protected TreeNode<T> clone() throws CloneNotSupportedException {
        TreeNode<T> clone = new TreeNode<>(this.content);
        clone.isExpand = this.isExpand;
        return clone;
    }
}
