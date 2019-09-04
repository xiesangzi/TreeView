package com.yhms.treelib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yhms.crumb.CrumbLinearLayout;
import com.yhms.crumb.CrumbModel;
import com.yhms.treelib.TreeNode;
import com.yhms.treelib.TreeViewAdapter;
import com.yhms.treelib.bean.GroupBean;
import com.yhms.treelib.binder.GroupBinder;
import com.yhms.treelib.binder.MemberBinder;
import com.yhms.treelib.binder.OnItemChildListener;
import com.yhms.treelib.binder.OnItemListener;

import java.util.Arrays;
import java.util.List;

/**
 * @功能描述:
 * @author: 邪桑子
 * @date: 2019/8/31 09:38
 */
public class TreeLinearLayout extends LinearLayout {
    private Context mContext;
    private RecyclerView recyclerView;
    private CrumbLinearLayout crumbLinearLayout;
    private TreeViewAdapter treeAdapter;
    private List<TreeNode> nodes;
    private OnItemChildListener onItemChildListener;
    private OnItemListener onItemListener;

    public void setOnItemChildListener(OnItemChildListener onItemChildListener) {
        this.onItemChildListener = onItemChildListener;
        treeAdapter.setOnItemChildListener(this.onItemChildListener);
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public TreeLinearLayout(Context context) {
        this(context, null);
    }

    public TreeLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TreeLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.setOrientation(VERTICAL);
        addCrumbView();
        addTreeView();
    }

    private void addCrumbView() {
        crumbLinearLayout = new CrumbLinearLayout(mContext);
        crumbLinearLayout.addItem(new CrumbModel("联系人", nodes));
        crumbLinearLayout.setOnClickItemListener((v, crumb) -> {
            if (crumb.getNodes() != null && !crumb.getNodes().isEmpty()) {
                treeAdapter.refresh(crumb.getNodes());
            } else {
                treeAdapter.refresh(nodes);
            }
        });
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(crumbLinearLayout, layoutParams);
    }

    private int dpToPx(float dpValue) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void addTreeView() {
        recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        treeAdapter = new TreeViewAdapter(mContext, Arrays.asList(new MemberBinder(), new GroupBinder()));
        treeAdapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
            @Override
            public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder, int position) {
                if (!node.isLeaf()) {
                    GroupBean groupBean = (GroupBean) node.getContent();
                    if (node.hasParent()) {
                        crumbLinearLayout.addItem(new CrumbModel(groupBean.getLabel(), node.getParent().getChildren()));
                    } else {
                        crumbLinearLayout.addItem(new CrumbModel(groupBean.getLabel(), nodes));
                    }
                    treeAdapter.refresh(node.getChildren());
                    crumbLinearLayout.updateLastView();
                }
                if (onItemListener != null) {
                    onItemListener.onClick(node, holder.itemView, position);
                }
                return true;
            }

            @Override
            public void onToggle(boolean isExpand, RecyclerView.ViewHolder holder) {
            }
        });
        recyclerView.setAdapter(treeAdapter);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        this.addView(recyclerView, layoutParams);
    }

    public void setNodes(List<TreeNode> nodes) {
        this.nodes = nodes;
        this.treeAdapter.refresh(this.nodes);
    }
}
