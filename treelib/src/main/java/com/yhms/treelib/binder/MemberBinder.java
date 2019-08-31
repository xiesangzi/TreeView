package com.yhms.treelib.binder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yhms.treelib.R;
import com.yhms.treelib.TreeNode;
import com.yhms.treelib.TreeViewBinder;
import com.yhms.treelib.bean.MemberBean;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by tlh on 2016/10/1 :)
 */

public class MemberBinder extends TreeViewBinder<MemberBinder.ViewHolder> {
    private OnItemChildListener onItemChildListener;

    @Override
    public ViewHolder provideViewHolder(View itemView, OnItemChildListener onItemChildListener) {
        this.onItemChildListener = onItemChildListener;
        return new ViewHolder(itemView);
    }

    @Override
    public void bindView(ViewHolder holder, int position, TreeNode node) {
        MemberBean member = (MemberBean) node.getContent();
        holder.tvName.setText(member.getName());
        if (!TextUtils.isEmpty(member.getPhone())) {
            holder.tvPhone.setText(member.getPhone());
        }
        if (!TextUtils.isEmpty(member.getUserIcon())) {
            Picasso.get().load(member.getUserIcon()).placeholder(R.mipmap.default_head).into(holder.ivMember);
        }
        holder.ivPhone.setTag("phone");
        holder.ivPhone.setOnClickListener(view -> {
            if (onItemChildListener != null) {
                onItemChildListener.onClick(node, view, position);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_member;
    }

    public class ViewHolder extends TreeViewBinder.ViewHolder {
        public TextView tvName;
        public TextView tvPhone;
        public CircleImageView ivMember;
        public ImageView ivPhone;

        public ViewHolder(View rootView) {
            super(rootView);
            this.tvName = rootView.findViewById(R.id.tv_name);
            this.tvPhone = rootView.findViewById(R.id.tv_phone);
            this.ivMember = rootView.findViewById(R.id.iv_member);
            this.ivPhone = rootView.findViewById(R.id.iv_phone);
        }
    }
}
