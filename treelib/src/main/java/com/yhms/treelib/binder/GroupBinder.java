package com.yhms.treelib.binder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yhms.treelib.R;
import com.yhms.treelib.TreeNode;
import com.yhms.treelib.TreeViewBinder;
import com.yhms.treelib.bean.GroupBean;


/**
 * Created by tlh on 2016/10/1 :)
 */

public class GroupBinder extends TreeViewBinder<GroupBinder.ViewHolder> {
    private OnItemChildListener onItemChildListener;

    @Override
    public ViewHolder provideViewHolder(View itemView, OnItemChildListener onItemChildListener) {
        this.onItemChildListener = onItemChildListener;
        return new ViewHolder(itemView);
    }

    @Override
    public void bindView(ViewHolder holder, int position, TreeNode node) {
        GroupBean group = (GroupBean) node.getContent();
        holder.tvName.setText(group.getLabel());
        if (group.getTotal() > 0) {
            holder.tvTotal.setText("(" + group.getTotal() + ")");
            holder.tvTotal.setVisibility(View.VISIBLE);
        } else {
            holder.tvTotal.setVisibility(View.GONE);
        }

        if (node.isLeaf()) {
            holder.ivArrow.setVisibility(View.INVISIBLE);
        } else {
            holder.ivArrow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_group;
    }

    public static class ViewHolder extends TreeViewBinder.ViewHolder {
        private ImageView ivArrow;
        private TextView tvName;
        private TextView tvTotal;

        public ViewHolder(View rootView) {
            super(rootView);
            this.ivArrow = rootView.findViewById(R.id.iv_arrow);
            this.tvName = rootView.findViewById(R.id.tv_name);
            this.tvTotal = rootView.findViewById(R.id.tv_total);
        }

        public ImageView getIvArrow() {
            return ivArrow;
        }

        public TextView getTvName() {
            return tvName;
        }
    }
}
