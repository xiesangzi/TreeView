package com.yhms.treeview;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.yhms.treelib.TreeNode;
import com.yhms.treelib.TreeNodeUtil;
import com.yhms.treelib.bean.ContactsBean;
import com.yhms.treelib.bean.MemberBean;
import com.yhms.treelib.binder.OnItemChildListener;
import com.yhms.treelib.binder.OnItemListener;
import com.yhms.treelib.view.TreeLinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TreeLinearLayout treeLinearLayout;
    private List<TreeNode> nodes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        treeLinearLayout = findViewById(R.id.treeView);
        treeLinearLayout.setOnItemChildListener(new OnItemChildListener() {
            @Override
            public boolean onClick(TreeNode node, View view, int position) {
                if ("phone".equals(view.getTag())) {
                    Toast.makeText(getApplicationContext(), "打电话吗？", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        treeLinearLayout.setOnItemListener(new OnItemListener() {
            @Override
            public boolean onClick(TreeNode node, View itemView, int position) {
                if (node.isLeaf()) {
                    Toast.makeText(getApplicationContext(), "查看详情吗？", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        String json = "[{\"pid\":\"-1\",\"id\":\"355724367998758912\",\"label\":\"古代帝王\",\"total\":2,\"members\":[{\"id\":\"360861734602551296\",\"userId\":\"262582648063668224\",\"label\":\"成吉思汗\",\"userIcon\":\"https://gss2.bdstatic.com/9fo3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=9b9218a95182b2b7a79f3ec20996acd2/aa64034f78f0f7369515ccac0255b319eac413b7.jpg\",\"userLevel\":1,\"phone\":\"13519141416\",\"userType\":1},{\"id\":\"360861734602551297\",\"userId\":\"356031873820143616\",\"label\":\"李世民\",\"userIcon\":\"https://gss1.bdstatic.com/9vo3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=56153736272dd42a5f0906ad3b003c88/dcc451da81cb39db37c20509d2160924ab18306a.jpg\",\"userLevel\":2,\"phone\":\"13885513387\",\"userType\":1}],\"children\":[]},{\"pid\":\"-1\",\"id\":\"360885240484605952\",\"label\":\"古代帝王2\",\"total\":2,\"members\":[{\"id\":\"360886277790511104\",\"userId\":\"262582648063668224\",\"label\":\"武则天\",\"userIcon\":\"https://gss1.bdstatic.com/-vo3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=147fc806d054564ee565e33f8be5fbbf/10dfa9ec8a1363273ffb36269c8fa0ec08fac795.jpg\",\"userLevel\":1,\"phone\":\"155918161466\",\"userType\":1},{\"id\":\"360886277794705408\",\"userId\":\"356031873820143616\",\"label\":\"秦始皇\",\"userIcon\":\"https://gss2.bdstatic.com/-fo3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=13d6727d28a446237ecaa264a0191533/3ac79f3df8dcd100c455f0267a8b4710b8122fef.jpg\",\"userLevel\":2,\"phone\":\"13985518887\",\"userType\":1}],\"children\":[]}]";
        List<ContactsBean> beans = JSON.parseArray(json, ContactsBean.class);
        nodes = TreeNodeUtil.getTreeNodes(beans);
        nodes.add(new TreeNode(new MemberBean("1000", "哪吒", "15919191919", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1567255788504&di=c4d222fe332db1d644ddd773e7d7a1c4&imgtype=0&src=http%3A%2F%2Fpix1.tvzhe.com%2Fstills%2Fmovie%2F223%2F20%2Fl%2FMB0nMBKlK%3D.jpg")));
        treeLinearLayout.setNodes(nodes);
    }
}
