package com.yhms.treelib;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.yhms.treelib.binder.OnItemChildListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 功能描述：树适配器
 *
 * @author 邪桑子
 * @date 2019/8/31 10:17
 */
public class TreeViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String KEY_IS_EXPAND = "IS_EXPAND";
    private final List<? extends TreeViewBinder> viewBinders;
    private List<TreeNode> displayNodes;
    private OnTreeNodeListener onTreeNodeListener;
    private boolean toCollapseChild;
    private Context mContext;
    private int topMargin;
    private int topDfMargin;
    private OnItemChildListener onItemChildListener;

    public void setOnItemChildListener(OnItemChildListener onItemChildListener) {
        this.onItemChildListener = onItemChildListener;
    }

    public TreeViewAdapter(Context context, List<? extends TreeViewBinder> viewBinders) {
        this(context, null, viewBinders);
    }

    public TreeViewAdapter(Context context, List<TreeNode> nodes, List<? extends TreeViewBinder> viewBinders) {
        this.mContext = context;
        this.displayNodes = new ArrayList<>();
        if (nodes != null) {
            findDisplayNodes(nodes);
        }
        this.viewBinders = viewBinders;
        this.onItemChildListener = onItemChildListener;
        topMargin = dpToPx(20f);
        topDfMargin = dpToPx(5f);
    }

    /**
     * 从nodes的结点中寻找展开了的非叶结点，添加到displayNodes中。
     *
     * @param nodes 基准点
     */
    private void findDisplayNodes(List<TreeNode> nodes) {
        for (TreeNode node : nodes) {
            displayNodes.add(node);
            if (!node.isLeaf() && node.isExpand()) {
                findDisplayNodes(node.getChildren());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return displayNodes.get(position).getContent().getLayoutId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        if (viewBinders.size() == 1) {
            return viewBinders.get(0).provideViewHolder(v, onItemChildListener);
        }
        for (TreeViewBinder viewBinder : viewBinders) {
            if (viewBinder.getLayoutId() == viewType) {
                return viewBinder.provideViewHolder(v, onItemChildListener);
            }
        }
        return viewBinders.get(0).provideViewHolder(v, onItemChildListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads != null && !payloads.isEmpty()) {
            Bundle b = (Bundle) payloads.get(0);
            for (String key : b.keySet()) {
                switch (key) {
                    case KEY_IS_EXPAND:
                        if (onTreeNodeListener != null) {
                            onTreeNodeListener.onToggle(b.getBoolean(key), holder);
                        }
                        break;
                }
            }
        }
        super.onBindViewHolder(holder, position, payloads);
    }

    private int dpToPx(float dpValue) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        if (displayNodes.get(position).isLeaf()) {
            boolean done = isAllLeaf(displayNodes.get(position).getParent());
            if (done) {
                layoutParams.topMargin = topDfMargin;
            } else {
                layoutParams.topMargin = topMargin;
            }
        } else {
            layoutParams.topMargin = topDfMargin;
        }
        holder.itemView.setLayoutParams(layoutParams);
        holder.itemView.setOnClickListener(v -> {
            TreeNode selectedNode = displayNodes.get(holder.getLayoutPosition());
            // Prevent multi-click during the short interval.
            try {
                long lastClickTime = (long) holder.itemView.getTag();
                if (System.currentTimeMillis() - lastClickTime < 500) {
                    return;
                }
            } catch (Exception e) {
                holder.itemView.setTag(System.currentTimeMillis());
            }
            holder.itemView.setTag(System.currentTimeMillis());

            if (onTreeNodeListener != null && onTreeNodeListener.onClick(selectedNode, holder, position)) {
                return;
            }
            if (selectedNode.isLeaf()) {
                return;
            }
            // This TreeNode was locked to click.
            if (selectedNode.isLocked()) {
                return;
            }
            boolean isExpand = selectedNode.isExpand();
            int positionStart = displayNodes.indexOf(selectedNode) + 1;
            if (!isExpand) {
                notifyItemRangeInserted(positionStart, addChildNodes(selectedNode, positionStart));
            } else {
                notifyItemRangeRemoved(positionStart, removeChildNodes(selectedNode, true));
            }
        });
        for (TreeViewBinder viewBinder : viewBinders) {
            if (viewBinder.getLayoutId() == displayNodes.get(position).getContent().getLayoutId()) {
                viewBinder.bindView(holder, position, displayNodes.get(position));
            }
        }
    }

    private boolean isAllLeaf(TreeNode parent) {
        if (parent == null) {
            return true;
        }
        List<TreeNode> list = parent.getChildren();
        for (TreeNode node : list) {
            if (!node.isLeaf()) {
                return false;
            }
        }
        return true;
    }

    private int addChildNodes(TreeNode pNode, int startIndex) {
        List<TreeNode> childList = pNode.getChildren();
        int addChildCount = 0;
        for (TreeNode treeNode : childList) {
            displayNodes.add(startIndex + addChildCount++, treeNode);
            if (treeNode.isExpand()) {
                addChildCount += addChildNodes(treeNode, startIndex + addChildCount);
            }
        }
        if (!pNode.isExpand()) {
            pNode.toggle();
        }
        return addChildCount;
    }

    private int removeChildNodes(TreeNode pNode) {
        return removeChildNodes(pNode, true);
    }

    private int removeChildNodes(TreeNode pNode, boolean shouldToggle) {
        if (pNode.isLeaf()) {
            return 0;
        }
        List<TreeNode> childList = pNode.getChildren();
        int removeChildCount = childList.size();
        displayNodes.removeAll(childList);
        for (TreeNode child : childList) {
            if (child.isExpand()) {
                if (toCollapseChild) {
                    child.toggle();
                }
                removeChildCount += removeChildNodes(child, false);
            }
        }
        if (shouldToggle) {
            pNode.toggle();
        }
        return removeChildCount;
    }

    @Override
    public int getItemCount() {
        return displayNodes == null ? 0 : displayNodes.size();
    }

    public void ifCollapseChildWhileCollapseParent(boolean toCollapseChild) {
        this.toCollapseChild = toCollapseChild;
    }

    public void setOnTreeNodeListener(OnTreeNodeListener onTreeNodeListener) {
        this.onTreeNodeListener = onTreeNodeListener;
    }

    public interface OnTreeNodeListener {
        /**
         * called when TreeNodes were clicked.
         * @return weather consume the click event.
         */
        boolean onClick(TreeNode node, RecyclerView.ViewHolder holder, int position);

        /**
         * called when TreeNodes were toggle.
         * @param isExpand the status of TreeNodes after being toggled.
         */
        void onToggle(boolean isExpand, RecyclerView.ViewHolder holder);
    }

    public void refresh(List<TreeNode> treeNodes) {
        displayNodes.clear();
        findDisplayNodes(treeNodes);
        notifyDataSetChanged();
    }

    public Iterator<TreeNode> getDisplayNodesIterator() {
        return displayNodes.iterator();
    }

    private void notifyDiff(final List<TreeNode> temp) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return temp.size();
            }

            @Override
            public int getNewListSize() {
                return displayNodes.size();
            }

            // judge if the same items
            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return TreeViewAdapter.this.areItemsTheSame(temp.get(oldItemPosition), displayNodes.get(newItemPosition));
            }

            // if they are the same items, whether the contents has bean changed.
            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return TreeViewAdapter.this.areContentsTheSame(temp.get(oldItemPosition), displayNodes.get(newItemPosition));
            }

            @Nullable
            @Override
            public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                return TreeViewAdapter.this.getChangePayload(temp.get(oldItemPosition), displayNodes.get(newItemPosition));
            }
        });
        diffResult.dispatchUpdatesTo(this);
    }

    private Object getChangePayload(TreeNode oldNode, TreeNode newNode) {
        Bundle diffBundle = new Bundle();
        if (newNode.isExpand() != oldNode.isExpand()) {
            diffBundle.putBoolean(KEY_IS_EXPAND, newNode.isExpand());
        }
        if (diffBundle.size() == 0) {
            return null;
        }
        return diffBundle;
    }

    // For DiffUtil, if they are the same items, whether the contents has bean changed.
    private boolean areContentsTheSame(TreeNode oldNode, TreeNode newNode) {
        return oldNode.getContent() != null && oldNode.getContent().equals(newNode.getContent())
                && oldNode.isExpand() == newNode.isExpand();
    }

    // judge if the same item for DiffUtil
    private boolean areItemsTheSame(TreeNode oldNode, TreeNode newNode) {
        return oldNode.getContent() != null && oldNode.getContent().equals(newNode.getContent());
    }

    /**
     * collapse all root nodes.
     */
    public void collapseAll() {
        // Back up the nodes are displaying.
        List<TreeNode> temp = backupDisplayNodes();
        //find all root nodes.
        List<TreeNode> roots = new ArrayList<>();
        for (TreeNode displayNode : displayNodes) {
            if (displayNode.isRoot()) {
                roots.add(displayNode);
            }
        }
        //Close all root nodes.
        for (TreeNode root : roots) {
            if (root.isExpand()) {
                removeChildNodes(root);
            }
        }
        notifyDiff(temp);
    }

    @NonNull
    private List<TreeNode> backupDisplayNodes() {
        List<TreeNode> temp = new ArrayList<>();
        for (TreeNode displayNode : displayNodes) {
            try {
                temp.add(displayNode.clone());
            } catch (CloneNotSupportedException e) {
                temp.add(displayNode);
            }
        }
        return temp;
    }

    public void collapseNode(TreeNode pNode) {
        List<TreeNode> temp = backupDisplayNodes();
        removeChildNodes(pNode);
        notifyDiff(temp);
    }

    public void collapseBrotherNode(TreeNode pNode) {
        List<TreeNode> temp = backupDisplayNodes();
        if (pNode.isRoot()) {
            List<TreeNode> roots = new ArrayList<>();
            for (TreeNode displayNode : displayNodes) {
                if (displayNode.isRoot()) {
                    roots.add(displayNode);
                }
            }
            //Close all root nodes.
            for (TreeNode root : roots) {
                if (root.isExpand() && !root.equals(pNode)) {
                    removeChildNodes(root);
                }
            }
        } else {
            TreeNode parent = pNode.getParent();
            if (parent == null) {
                return;
            }
            List<TreeNode> childList = parent.getChildren();
            for (TreeNode node : childList) {
                if (node.equals(pNode) || !node.isExpand()) {
                    continue;
                }
                removeChildNodes(node);
            }
        }
        notifyDiff(temp);
    }

}
