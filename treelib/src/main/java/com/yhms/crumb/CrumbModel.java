package com.yhms.crumb;

import com.yhms.treelib.TreeNode;

import java.util.List;

/**
 * @功能描述:
 * @author: 邪桑子
 * @date: 2019/8/30 16:39
 */
public class CrumbModel {
    private String title;
    private List<TreeNode> nodes;

    public CrumbModel(String title, List<TreeNode> nodes) {
        this.title = title;
        this.nodes = nodes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TreeNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<TreeNode> nodes) {
        this.nodes = nodes;
    }

}
