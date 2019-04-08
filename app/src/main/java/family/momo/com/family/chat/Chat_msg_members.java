package family.momo.com.family.chat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import family.momo.com.family.R;
import family.momo.com.family.information.Infomation_invitecircle;
import family.momo.com.family.information.Infomation_sharecircle_share;

public class Chat_msg_members extends AppCompatActivity {

    public static Activity chat_members_Activity;
    String OTHERID;
    //ActionBar
    ImageView chat_msg_members_actionbar_img_back;
    TextView chat_msg_members_txt_total;
    LinearLayout chat_msg_members_style_layout_invite;

    //列表块
    ListView chat_msg_members_listview_groups ;
    ArrayList<Map<String, Object>> datas = new ArrayList<>();//定义全局数据集合
    List<String> userNames = new LinkedList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_msg_members);
        chat_members_Activity = this;
        OTHERID = getIntent().getStringExtra("otherid");
        setActionBar();
        initViews();
        getGroups();
        shouGroups();
    }
    //设置Actionbar和状态栏
    private void setActionBar(){
        View decorView = getWindow().getDecorView();
        //decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);//隐藏状态栏
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getSupportActionBar().hide();//隐藏Actionbar
    }
    //初始化组件
    private void initViews(){
        chat_msg_members_style_layout_invite = findViewById(R.id.chat_msg_members_style_layout_invite);
        chat_msg_members_style_layout_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat_msg_members.this,Infomation_sharecircle_share.class);
                intent.putExtra("groupcode","666689");
                startActivity(intent);
            }
        });
        chat_msg_members_txt_total = findViewById(R.id.chat_msg_members_txt_total);
        chat_msg_members_listview_groups = findViewById(R.id.chat_msg_members_listview_groups);
        chat_msg_members_actionbar_img_back = findViewById(R.id.chat_msg_members_actionbar_img_back);
        chat_msg_members_actionbar_img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //获取群组信息
    public void getGroups(){
        userNames.add("宋慧宇");
        userNames.add("余里洁");
        userNames.add("大王");
    }
    //显示群组信息
    public void shouGroups(){

        for (int i = 0; i < userNames.size(); i++){
            Map<String, Object> item = new HashMap<>();
            item.put("name", userNames.get(i));
            datas.add(item);
        }//获得simpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(Chat_msg_members.this,datas,R.layout.activity_chat_msg_members_style,new String[]{ "name"},new int[]{R.id.chat_msg_members_style_txt_name});
        chat_msg_members_listview_groups.setAdapter(simpleAdapter);
        chat_msg_members_listview_groups.setOnItemClickListener(new ChooseGroupClick());
        chat_msg_members_txt_total.setText("该小圈圈共"+userNames.size()+"个成员");
    }
    //项目点击事件
    class ChooseGroupClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(Chat_msg_members.this,Chat_msg_members_users.class);
            intent.putExtra("otherid",userNames.get(position));
            startActivity(intent);
        }
    }
}
